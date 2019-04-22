package com.olacabs.jackhammer.application;

import javax.servlet.DispatcherType;

import java.util.EnumSet;

import com.hubspot.dropwizard.guice.GuiceBundle;

import com.olacabs.jackhammer.filters.AuthorizationFilter;
import com.olacabs.jackhammer.git.manager.GitPooler;
import com.olacabs.jackhammer.scan.manager.ScheduledScanPooler;
import com.olacabs.jackhammer.scan.manager.WpScanSchedulerPooler;
import com.olacabs.jackhammer.tool.interfaces.container.manager.AutoScalingManager;
import com.olacabs.jackhammer.tool.interfaces.container.manager.HangedToolInstanceManager;
import com.olacabs.jackhammer.tool.interfaces.container.manager.ActiveToolInstanceManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.websockets.WebsocketBundle;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.skife.jdbi.v2.DBI;

import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.scan.manager.ScanPooler;
import com.olacabs.jackhammer.tool.interfaces.sdk.bridge.SdkCommunicator;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.controllers.*;
import com.olacabs.jackhammer.filters.AuthenticationFilter;
import com.olacabs.jackhammer.filters.CORSFilter;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;


@Slf4j
public class JackhammerService extends Application<JackhammerConfiguration> {

    private WebsocketBundle websocketBundle;
    private GuiceBundle guiceBundle = GuiceBundle.<JackhammerConfiguration>newBuilder()
            .addModule(new JackhammerBinder())
            .enableAutoConfig(getClass().getPackage().getName())
            .setConfigClass(JackhammerConfiguration.class)
            .build();


    public static void main(String[] args) throws Exception {
        new JackhammerService().run(args);
    }

    @Override
    public void initialize(final Bootstrap<JackhammerConfiguration> bootstrap) {
        websocketBundle = new WebsocketBundle(SdkCommunicator.class);
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(websocketBundle);
//        bootstrap.addBundle(new SwaggerBundle<JackhammerConfiguration>() {
//            @Override
//            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(JackhammerConfiguration jackhammerConfiguration) {
//                return jackhammerConfiguration.swaggerBundleConfiguration;
//            }
//        });
    }

    @Override
    public void run(JackhammerConfiguration jackhammerConfiguration, Environment environment) throws Exception {

        final DBIFactory factory = new DBIFactory();
        final DBI mysqlDB = factory.build(environment, jackhammerConfiguration.getDatabase(), Constants.MYSQL_DB);
        DBFactory dbFactory = new DBFactory(mysqlDB);

        //register filters
        environment.servlets().addFilter(Constants.CORS_FILTER, CORSFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, Constants.CORS_URL_PATTERN);
        environment.servlets()
                .addFilter(Constants.AUTHENTICATION_FILTER, (AuthenticationFilter) guiceBundle
                        .getInjector()
                        .getInstance(AuthenticationFilter.class))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, Constants.AUTH_BASE_URL_PATTERN);
        environment.servlets()
                .addFilter(Constants.AUTHORIZATION_FILTER, (AuthorizationFilter) guiceBundle
                        .getInjector()
                        .getInstance(AuthorizationFilter.class))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, Constants.AUTH_BASE_URL_PATTERN);
        //register resources
        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(UserAccountsController.class);
        environment.jersey().register(UsersController.class);
        environment.jersey().register(GroupsController.class);
        environment.jersey().register(RolesController.class);
        environment.jersey().register(PermissionsController.class);
        environment.jersey().register(ScansController.class);
        environment.jersey().register(ReposController.class);
        environment.jersey().register(ScheduleTypesController.class);
        environment.jersey().register(FindingsController.class);
        environment.jersey().register(CommentsController.class);
        environment.jersey().register(TagsController.class);
        environment.jersey().register(UploadsController.class);
        environment.jersey().register(ToolsController.class);
        environment.jersey().register(LanguagesController.class);
        environment.jersey().register(ScanTypesController.class);
        environment.jersey().register(FiltersController.class);
        environment.jersey().register(ActionsController.class);
        environment.jersey().register(TasksController.class);
        environment.jersey().register(GitController.class);
        environment.jersey().register(SMTPDetailsController.class);
        environment.jersey().register(JiraDetailsController.class);
        environment.jersey().register(DefaultRolesController.class);
        environment.jersey().register(SeverityLevelsController.class);
        environment.jersey().register(HardcodeSecretsController.class);
        environment.jersey().register(DashboardsController.class);
        environment.jersey().register(ApplicationsController.class);
        environment.jersey().register(AnalyticsController.class);
        environment.jersey().register(JchTasksController.class);
        environment.jersey().register(ResetPasswordController.class);

        //adding thread pool
        environment.lifecycle().manage(guiceBundle.getInjector().getInstance(ScanPooler.class));
        environment.lifecycle().manage(guiceBundle.getInjector().getInstance(ScheduledScanPooler.class));
        environment.lifecycle().manage(guiceBundle.getInjector().getInstance(ActiveToolInstanceManager.class));
        environment.lifecycle().manage(guiceBundle.getInjector().getInstance(GitPooler.class));
        environment.lifecycle().manage(guiceBundle.getInjector().getInstance(HangedToolInstanceManager.class));
        environment.lifecycle().manage(guiceBundle.getInjector().getInstance(WpScanSchedulerPooler.class));

        if (jackhammerConfiguration.getToolManagerConfiguration().getEnableAutoScaling())
            environment.lifecycle().manage(guiceBundle.getInjector().getInstance(AutoScalingManager.class));

    }

    @Override
    public String getName() {
        return Constants.APPLICATION_NAME;
    }

}
