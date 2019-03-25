package com.olacabs.jackhammer.application;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.AbstractModule;

import com.olacabs.jackhammer.filters.AuthorizationFilter;
import com.olacabs.jackhammer.models.Task;
import com.olacabs.jackhammer.scan.manager.*;
import com.olacabs.jackhammer.utilities.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.olacabs.jackhammer.git.manager.GitPooler;
import com.olacabs.jackhammer.git.manager.ReposPuller;
import com.olacabs.jackhammer.tool.interfaces.container.manager.*;
import com.olacabs.jackhammer.tool.interfaces.response.ToolResponse;


import com.olacabs.jackhammer.configuration.WebSocketsConfiguration;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.filters.AuthenticationFilter;
import com.olacabs.jackhammer.handler.*;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.response.builder.*;
import com.olacabs.jackhammer.security.JwtSecurity;
import com.olacabs.jackhammer.service.*;
import com.olacabs.jackhammer.tool.interfaces.request.ScanRequest;
import com.olacabs.jackhammer.tool.interfaces.response.ScanResponse;
import com.olacabs.jackhammer.tool.interfaces.sdk.bridge.SdkCommunicator;
import com.olacabs.jackhammer.validations.*;
import com.olacabs.jackhammer.validations.factories.ValidatorBuilderFactory;
import com.olacabs.jackhammer.exceptions.handlers.ExceptionHandler;
import com.olacabs.jackhammer.service.factories.DataServiceBuilderFactory;
import com.olacabs.jackhammer.handler.factories.HandlerFactory;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.response.builder.factories.ResponseBuilderFactory;


public class JackhammerBinder extends AbstractModule {

    @Override
    protected void configure() {

        //handlers
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.USER_ACCOUNTS_HANDLER)).to(UserAccountsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.CHANGE_PASSWORD_HANDLER)).to(ChangePasswordHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.RESET_PASSWORD_HANDLER)).to(ResetPasswordHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.USERS_HANDLER)).to(UsersHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.GROUPS_HANDLER)).to(GroupsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.ROLES_HANDLER)).to(RolesHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.PERMISSIONS_HANDLER)).to(PermissionsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.SCANS_HANDLER)).to(ScansHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.REPOS_HANDLER)).to(ReposHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.SCHEDULE_TYPES_HANDLER)).to(ScheduleTypesHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.FINDING_HANDLER)).to(FindingsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.COMMENT_HANDLER)).to(CommentsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.TAG_HANDLER)).to(TagsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.UPLOAD_HANDLER)).to(UploadsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.TOOL_HANDLER)).to(ToolsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.LANGUAGE_HANDLER)).to(LanguagesHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.SCAN_TYPE_HANDLER)).to(ScanTypesHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.ACTION_HANDLER)).to(ActionsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.TASK_HANDLER)).to(TasksHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.GIT_HANDLER)).to(GitHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.SMTP_DETAILS_HANDLER)).to(SMTPDetailsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.JIRA_DETAILS_HANDLER)).to(JiraDetailsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.DEFAULT_ROLE_HANDLER)).to(DefaultRolesHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.HARDCODE_SECRET_HANDLER)).to(HardcodeSecretHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.SEVERITY_LEVEL_HANDLER)).to(SeverityLevelsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.DASHBOARD_HANDLER)).to(DashboardsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.APPLICATION_HANDLER)).to(ApplicationsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.ANALYTICS_HANDLER)).to(AnalyticsHandler.class);
        bind(AbstractHandler.class).annotatedWith(Names.named(Constants.FILTERS_HANDLER)).to(FiltersHandler.class);

        //services
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.CHANGE_PASSWORD_DATA_SERVICE)).to(ChangePasswordDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.RESET_PASSWORD_DATA_SERVICE)).to(ResetPasswordDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.USER_DATA_SERVICE)).to(UserDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.JWT_DATA_SERVICE)).to(JwtDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.GROUP_DATA_SERVICE)).to(GroupDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.ROLE_DATA_SERVICE)).to(RoleDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.PERMISSION_DATA_SERVICE)).to(PermissionDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.SCAN_DATA_SERVICE)).to(ScanDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.REPO_DATA_SERVICE)).to(RepoDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.SCHEDULE_TYPE_DATA_SERVICE)).to(ScheduleTypeDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.LANGUAGE_DATA_SERVICE)).to(LanguageDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.TOOL_DATA_SERVICE)).to(ToolDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.FINDING_DATA_SERVICE)).to(FindingDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.COMMENT_DATA_SERVICE)).to(CommentDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.TAG_DATA_SERVICE)).to(TagDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.UPLOAD_DATA_SERVICE)).to(UploadDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.TOOL_DATA_SERVICE)).to(ToolDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.LANGUAGE_DATA_SERVICE)).to(LanguageDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.SCAN_TYPE_DATA_SERVICE)).to(ScanTypeDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.ACTION_DATA_SERVICE)).to(ActionDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.TASK_DATA_SERVICE)).to(TaskDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.GIT_DATA_SERVICE)).to(GitDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.SMTP_DETAIL_DATA_SERVICE)).to(SMTPDetailDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.JIRA_DETAIL_DATA_SERVICE)).to(JiraDetailDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.DEFAULT_ROLE_DATA_SERVICE)).to(DefaultRoleDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.HARDCODE_SECRET_DATA_SERVICE)).to(HardcodeSecretDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.SEVERITY_LEVEL_DATA_SERVICE)).to(SeverityLevelDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.DASHBOARD_DATA_SERVICE)).to(DashboardDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.APPLICATION_DATA_SERVICE)).to(ApplicationDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.ANALYTICS_DATA_SERVICE)).to(AnalyticsDataService.class);
        bind(AbstractDataService.class).annotatedWith(Names.named(Constants.FILTER_DATA_SERVICE)).to(FilterDataService.class);

        //response builders
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.RESET_PASSWORD_RESPONSE_BUILDER)).to(ResetPasswordResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.CHANGE_PASSWORD_RESPONSE_BUILDER)).to(ChangePasswordResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.USER_RESPONSE_BUILDER)).to(UserResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.GROUP_RESPONSE_BUILDER)).to(GroupResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.ROLE_RESPONSE_BUILDER)).to(RoleResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.PERMISSION_RESPONSE_BUILDER)).to(PermissionResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.SCAN_RESPONSE_BUILDER)).to(ScanResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.REPO_RESPONSE_BUILDER)).to(RepoResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.SCHEDULE_TYPE_RESPONSE_BUILDER)).to(ScheduleTypeResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.FINDING_RESPONSE_BUILDER)).to(FindingResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.COMMENT_RESPONSE_BUILDER)).to(CommentResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.TAG_RESPONSE_BUILDER)).to(TagResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.UPLOAD_RESPONSE_BUILDER)).to(UploadResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.TOOL_RESPONSE_BUILDER)).to(ToolResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.LANGUAGE_RESPONSE_BUILDER)).to(LanguageResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.SCAN_TYPE_RESPONSE_BUILDER)).to(ScanTypeResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.ACTION_RESPONSE_BUILDER)).to(ActionResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.TASK_RESPONSE_BUILDER)).to(TaskResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.GIT_RESPONSE_BUILDER)).to(GitResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.SMTP_DETAIL_RESPONSE_BUILDER)).to(SMTPDetailResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.JIRA_DETAIL_RESPONSE_BUILDER)).to(JiraDetailResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.DEFAULT_ROLE_RESPONSE_BUILDER)).to(DefaultRoleResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.HARDCODE_SECRET_RESPONSE_BUILDER)).to(HardcodeSecretResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.SEVERITY_LEVEL_RESPONSE_BUILDER)).to(SeverityLevelResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.DASHBOARD_RESPONSE_BUILDER)).to(DashboardResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.APPLICATION_RESPONSE_BUILDER)).to(ApplicationResponseBuilder.class);
        bind(AbstractResponseBuilder.class).annotatedWith(Names.named(Constants.ANALYTICS_RESPONSE_BUILDER)).to(AnalyticsResponseBuilder.class);


        //validations
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.CHANGE_PASSWORD_VALIDATIONS)).to(ChangePasswordValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.RESET_PASSWORD_VALIDATIONS)).to(ResetPasswordValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.USER_VALIDATIONS)).to(UserValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.ROLE_VALIDATIONS)).to(RoleValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.GROUP_VALIDATIONS)).to(GroupValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.PERMISSION_VALIDATIONS)).to(PermissionValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.SCAN_VALIDATIONS)).to(ScanValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.REPO_VALIDATIONS)).to(RepoValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.SCHEDULE_TYPE_VALIDATIONS)).to(SchedulTypeValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.GIT_VALIDATIONS)).to(GitValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.SMTP_DETAILS_VALIDATOR)).to(SMTPDetailValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.JIRA_DETAILS_VALIDATOR)).to(JiraDetailValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.DEFAULT_ROLE_VALIDATOR)).to(DefaultRoleValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.HARDCODE_SECRET_VALIDATOR)).to(HardcodeSecretValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.SEVERITY_LEVEL_VALIDATOR)).to(SeverityLevelValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.SCAN_TYPE_VALIDATOR)).to(ScanTypeValidator.class);
        bind(AbstractValidator.class).annotatedWith(Names.named(Constants.TOOL_VALIDATIONS)).to(ToolValidator.class);


        //Bcrypt
        bind(PasswordEncoder.class).annotatedWith(Names.named(Constants.BCRYPT_PASSWORD_ENCODER)).to(BCryptPasswordEncoder.class);


        //filters
        bind(AuthenticationFilter.class);
        bind(AuthorizationFilter.class);

        //DAOJDBI
        bind(UserDAO.class).annotatedWith(Names.named(Constants.USER_DAO_JDBI)).to(UserDAOJdbi.class);
        bind(GroupDAO.class).annotatedWith(Names.named(Constants.GROUP_DAO_JDBI)).to(GroupDAOJdbi.class);
        bind(RoleDAO.class).annotatedWith(Names.named(Constants.ROLE_DAO_JDBI)).to(RoleDAOJdbi.class);

        //factories
        bind(HandlerFactory.class);
        bind(ResponseBuilderFactory.class);
        bind(DataServiceBuilderFactory.class);
        bind(ValidatorBuilderFactory.class);

        //security
        bind(JwtSecurity.class);

        //exception
        bind(ExceptionHandler.class);

        //Bcrypt
        bind(PasswordEncoder.class).annotatedWith(Names.named(Constants.BCRYPT_PASSWORD_ENCODER)).to(BCryptPasswordEncoder.class);

        //PagedResponse
        bind(PagedResponse.class);


        //Scan manager
        bind(ScanPooler.class);
        bind(ScanPicker.class);
        bind(ScheduledScanPooler.class);
        bind(ScheduledScanPicker.class);

        //Tool manager
        bind(ActiveToolInstanceManager.class);
        bind(ActiveToolInstanceHealthCheck.class);
        
        //WebsocketServerEndpoint
        bind(SdkCommunicator.class);
        
        //ScanUtil
        bind(ScanUtil.class);

        //InstanceManager
        bind(MarathonClientManager.class);

        //Mesos Container
        bind(DockerContainer.class);

        //HealthCheck
        bind(DockerHealthCheck.class);

        //web socket custom config
        requestStaticInjection(WebSocketsConfiguration.class);

        //scan request
        bind(ScanRequest.class);

        //scan response
        bind(ScanResponse.class);

        //file operations
        bind(FileOperations.class);

        //tool util
        bind(ToolUtil.class);

        //tool response
        bind(ToolResponse.class);

        //git pooler
        bind(GitPooler.class);

        // repo puller
        bind(ReposPuller.class);

        //git util
        bind(GitUtil.class);

        //jira client
        bind(JiraClient.class);

        //task model
        bind(Task.class);

        //email operations
        bind(EmailOperations.class);
        //Tool Instance check
        bind(HangedToolInstanceCheck.class);
        bind(HangedToolInstanceManager.class);
        //marathon client
//        bind(MarathonClient.class);
        bind(WpScanSchedulerPicker.class);
        bind(WpScanSchedulerPooler.class);

        //docker util
        bind(DockerUtil.class);
    }

    @Provides
    @Named(Constants.USER_DAO)
    public UserDAO getUserDBFactory(){
        return DBFactory.getDBI().onDemand(UserDAO.class);
    }

    @Provides
    @Named(Constants.ROLE_DAO)
    public RoleDAO getRoleDBFactory(){
        return DBFactory.getDBI().onDemand(RoleDAO.class);
    }

    @Provides
    @Named(Constants.ROLE_USER_DAO)
    public RoleUserDAO getRoleUserDBFactory(){
        return DBFactory.getDBI().onDemand(RoleUserDAO.class);
    }

    @Provides
    @Named(Constants.JWT_TOKEN_DAO)
    public JwtTokenDAO getJwtTokenDBFactory(){
        return DBFactory.getDBI().onDemand(JwtTokenDAO.class);
    }


    @Provides
    @Named(Constants.PERMISSION_DAO)
    public PermissionDAO getPermissionDBFactory(){
        return DBFactory.getDBI().onDemand(PermissionDAO.class);
    }

    @Provides
    @Named(Constants.ROLE_PERMISSION_DAO)
    public PermissionRoleDAO getActionPermissionDBFactory(){
        return DBFactory.getDBI().onDemand(PermissionRoleDAO.class);
    }

    @Provides
    @Named(Constants.GROUP_DAO)
    public GroupDAO getGroupDBFactory(){
        return DBFactory.getDBI().onDemand(GroupDAO.class);
    }

    @Provides
    @Named(Constants.GROUP_ROLE_DAO)
    public GroupRoleDAO getGroupRoleDBFactory(){
        return DBFactory.getDBI().onDemand(GroupRoleDAO.class);
    }

    @Provides
    @Named(Constants.GROUP_USER_DAO)
    public GroupUserDAO getGroupUserDBFactory(){
        return DBFactory.getDBI().onDemand(GroupUserDAO.class);
    }

    @Provides
    @Named(Constants.SCAN_DAO)
    public ScanDAO getScanDBFactory(){
        return DBFactory.getDBI().onDemand(ScanDAO.class);
    }

    @Provides
    @Named(Constants.REPO_DAO)
    public RepoDAO getRepoDBFactory(){
        return DBFactory.getDBI().onDemand(RepoDAO.class);
    }

    @Provides
    @Named(Constants.BRANCH_DAO)
    public BranchDAO getBranchDBFactory(){
        return DBFactory.getDBI().onDemand(BranchDAO.class);
    }

    @Provides
    @Named(Constants.SCHEDULE_TYPE_DAO)
    public ScheduleTypeDAO getScheduleTypeFactory(){
        return DBFactory.getDBI().onDemand(ScheduleTypeDAO.class);
    }

    @Provides
    @Named(Constants.TOOL_DAO)
    public ToolDAO getToolFactory(){ return DBFactory.getDBI().onDemand(ToolDAO.class); }

    @Provides
    @Named(Constants.LANGUAGE_DAO)
    public LanguageDAO getLanguageFactory(){
        return DBFactory.getDBI().onDemand(LanguageDAO.class);
    }

    @Provides
    @Named(Constants.SCAN_TOOL_DAO)
    public ScanToolDAO getScanToolFactory(){
        return DBFactory.getDBI().onDemand(ScanToolDAO.class);
    }

    @Provides
    @Named(Constants.FINDING_DAO)
    public FindingDAO getFindingFactory(){
        return DBFactory.getDBI().onDemand(FindingDAO.class);
    }

    @Provides
    @Named(Constants.COMMENT_DAO)
    public CommentDAO getCommentFactory(){
        return DBFactory.getDBI().onDemand(CommentDAO.class);
    }

    @Provides
    @Named(Constants.TAG_DAO)
    public TagDAO getTagFactory(){
        return DBFactory.getDBI().onDemand(TagDAO.class);
    }

    @Provides
    @Named(Constants.UPLOAD_DAO)
    public UploadDAO getUploadFactory(){
        return DBFactory.getDBI().onDemand(UploadDAO.class);
    }

    @Provides
    @Named(Constants.FINDING_TAG_DAO)
    public FindingTagDAO getFindingTagFactory(){
        return DBFactory.getDBI().onDemand(FindingTagDAO.class);
    }

    @Provides
    @Named(Constants.SCAN_TYPE_DAO)
    public ScanTypeDAO getScanTypeFactory(){
        return DBFactory.getDBI().onDemand(ScanTypeDAO.class);
    }

    @Provides
    @Named(Constants.TOOL_INSTANCE_DAO)
    public ToolInstanceDAO getToolInstanceFactory(){
        return DBFactory.getDBI().onDemand(ToolInstanceDAO.class);
    }

    @Provides
    @Named(Constants.ACTION_DAO)
    public ActionDAO getActionFactory(){
        return DBFactory.getDBI().onDemand(ActionDAO.class);
    }

    @Provides
    @Named(Constants.TASK_DAO)
    public TaskDAO getTaskFactory(){
        return DBFactory.getDBI().onDemand(TaskDAO.class);
    }

    @Provides
    @Named(Constants.ROLE_TASK_DAO)
    public RoleTaskDAO getTaskUserFactory(){
        return DBFactory.getDBI().onDemand(RoleTaskDAO.class);
    }

    @Provides
    @Named(Constants.OWNER_TYPE_DAO)
    public OwnerTypeDAO getOwnerTypeFactory(){
        return DBFactory.getDBI().onDemand(OwnerTypeDAO.class);
    }

    @Provides
    @Named(Constants.GIT_DAO)
    public GitDAO getGitFactory(){
        return DBFactory.getDBI().onDemand(GitDAO.class);
    }

    @Provides
    @Named(Constants.SMTP_DETAIL_DAO)
    public SMTPDetailDAO getSMTPDetailFactory(){
        return DBFactory.getDBI().onDemand(SMTPDetailDAO.class);
    }

    @Provides
    @Named(Constants.JIRA_DETAIL_DAO)
    public JiraDetailDAO getJiraDetailFactory(){
        return DBFactory.getDBI().onDemand(JiraDetailDAO.class);
    }

    @Provides
    @Named(Constants.DEFAULT_ROLE_DAO)
    public DefaultRoleDAO getDefaultRoleFactory(){
        return DBFactory.getDBI().onDemand(DefaultRoleDAO.class);
    }

    @Provides
    @Named(Constants.HARDCODE_SECRET_DAO)
    public HardcodeSecretDAO getHardcodeSecretFactory(){
        return DBFactory.getDBI().onDemand(HardcodeSecretDAO.class);
    }

    @Provides
    @Named(Constants.SEVERITY_LEVEL_DAO)
    public SeverityLevelDAO getSeverityLevelFactory(){
        return DBFactory.getDBI().onDemand(SeverityLevelDAO.class);
    }

    @Provides
    @Named(Constants.DASHBOARD_DAO)
    public DashboardDAO getDashboardFactory(){
        return DBFactory.getDBI().onDemand(DashboardDAO.class);
    }

    @Provides
    @Named(Constants.ANALYTICS_DAO)
    public AnalyticsDAO getAnalyticsFactory(){
        return DBFactory.getDBI().onDemand(AnalyticsDAO.class);
    }

    @Provides
    @Named(Constants.APPLICATION_DAO)
    public ApplicationDAO getApplicationFactory(){
        return DBFactory.getDBI().onDemand(ApplicationDAO.class);
    }

    @Provides
    @Named(Constants.FILTER_DAO)
    public FilterDAO getFilterFactory(){
        return DBFactory.getDBI().onDemand(FilterDAO.class);
    }
}

