package com.olacabs.jackhammer.tool.interfaces.container.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.ToolDAO;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.models.ToolManifest;
import com.olacabs.jackhammer.utilities.DockerUtil;
import com.olacabs.jackhammer.utilities.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.utils.MarathonException;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
public class ActiveToolInstanceHealthCheck implements Runnable {

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    MarathonClientManager marathonClientManager;

    @Inject
    ToolUtil toolUtil;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    DockerUtil dockerUtil;
    public static final Map<Long, String> toolStatus = new HashMap();

    public void run() {
        try {
            List<Tool> toolList = toolDAO.getAll();
            Boolean enabledMarathon = Boolean.valueOf(System.getenv(Constants.ENABLED_MARATHON));
            if (enabledMarathon) {
                containersHealthCheckWithMarathon(toolList);
            } else {
                containersHealthCheckWithNoMarathon(toolList);
            }
        } catch (Exception e) {
            log.error("Tool health check error", e);
        } catch (Throwable th) {
            log.error("Tool health check error", th);
        }
    }

    private void containersHealthCheckWithNoMarathon(List<Tool> toolList) {
        for (Tool tool : toolList) {
            ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
            String imageId = toolManifest.getContainer().getDocker().getImage();
            dockerUtil.pullImage(imageId);
            dockerUtil.startInstances(imageId, toolManifest);
            updateToolStatus(dockerUtil.getContainerImages().get(imageId), toolManifest.getInitialInstances(), tool);
        }
    }

    private void containersHealthCheckWithMarathon(List<Tool> toolList) {
        verifyMarathonHealthCheck();
        for (Tool eachTool : toolList) {
            int noOfToolsRunning = 0;
            int configuredInstances = 0;
            try {
                ToolManifest toolManifest = toolUtil.buildToolManifestRecord(eachTool);
                GetAppResponse getAppResponse = marathonClientManager.getApp(toolManifest.getId());
                if (getAppResponse != null) noOfToolsRunning = getRunningInstances(getAppResponse);
                configuredInstances = getConfiguredInstances(toolManifest);
                updateToolStatus(noOfToolsRunning, configuredInstances, eachTool);
                if (noOfToolsRunning == 0) marathonClientManager.createApp(toolManifest);
                if (noOfToolsRunning != 0 && noOfToolsRunning < configuredInstances)
                    marathonClientManager.updateApp(toolManifest);
                reDeployTool(eachTool, toolManifest.getId());
            } catch (MarathonException me) {
                updateToolStatus(noOfToolsRunning, configuredInstances, eachTool);
                log.error("MarathonException While checking the tool health check", me.getMessage());
            }
        }
    }

    private void updateToolStatus(int runningInstances, int configuredInstances, Tool tool) {
        String currentToolStatus = tool.getStatus();
        List<ToolInstance> toolInstanceList = toolInstanceDAO.getByToolId(tool.getId());
        if (runningInstances == configuredInstances && toolInstanceList.size() > 0) {
            tool.setStatus(runningInstances + Constants.STRING_SPACER + Constants.HEALTHY);
        } else if (runningInstances != 0 && runningInstances < configuredInstances) {
            StringBuilder healthyMessage = new StringBuilder();
            healthyMessage.append(runningInstances);
            healthyMessage.append(Constants.STRING_SPACER);
            healthyMessage.append(Constants.HEALTHY);
            healthyMessage.append(configuredInstances - runningInstances);
            healthyMessage.append(Constants.UN_HEALTHY);
            tool.setStatus(healthyMessage.toString());
        } else {
            tool.setStatus(Constants.UN_HEALTHY);
        }
        if (!StringUtils.equals(currentToolStatus, tool.getStatus())) toolDAO.updateStatus(tool);
    }

    private int getRunningInstances(GetAppResponse getAppResponse) {
        if (getAppResponse == null) return 0;
        return getAppResponse.getApp().getTasksRunning();
    }

    private int getConfiguredInstances(ToolManifest toolManifest) {
        if (toolManifest == null) return 0;
        return toolManifest.getInitialInstances();
    }

    //redeploy tool if session not present with jch
    private void reDeployTool(final Tool tool, final String appId) throws MarathonException {
        List<ToolInstance> toolInstances = toolInstanceDAO.getByToolId(tool.getId());
        if (toolInstances.size() > 0 || StringUtils.equals(toolStatus.get(tool.getId()), Constants.MONITORING))
            return;
        toolStatus.put(tool.getId(), Constants.MONITORING);
        log.info("Monitoring tool...{}",appId);
        waitUntilGracePeriod(tool, appId);
    }

    private int getDifferenceHours(Tool tool) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
        long milliseconds = timestamp.getTime() - tool.getUpdatedAt().getTime();
        final int seconds = (int) milliseconds / 1000;
        final int differenceHours = seconds / 3600;
        return differenceHours;
    }

    private void verifyMarathonHealthCheck() {
        String healthCheckEndpoint = jackhammerConfiguration.marathonConfiguration.getEndpoint() + Constants.MARATHON_HEALTH_API;
        Client client = ClientBuilder.newClient();
        client.target(healthCheckEndpoint).request().get(String.class);
    }

    private void waitUntilGracePeriod(final Tool tool, final String appId) {
        final int differenceHours = getDifferenceHours(tool);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                try {
                    List<ToolInstance> toolInstances = toolInstanceDAO.getByToolId(tool.getId());
                    if (toolInstances.size() == 0 && differenceHours > 1) {
                        log.info("Session not hold with jackhammer for tool id {} {} ", tool.getId(), appId);
                        marathonClientManager.deleteApp(appId);
                    }
                } catch (Exception e) {
                    log.error("Error while deleting an app {} {}..", appId);
                }
            }
        };
        int delay = 15;
        scheduler.schedule(task, delay, TimeUnit.MINUTES);
        scheduler.shutdown();
        toolStatus.remove(tool.getId());
    }
}
