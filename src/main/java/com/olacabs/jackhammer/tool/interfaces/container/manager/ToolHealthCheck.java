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
import com.olacabs.jackhammer.utilities.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.utils.MarathonException;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Calendar;
import java.util.List;


@Slf4j
public class ToolHealthCheck implements Runnable {

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

    public void run() {
        try {
            List<Tool> toolList = toolDAO.getAll();
            log.info("pooling tools and tools size => " + toolList.size());
            String healthCheckEndpoint = jackhammerConfiguration.marathonConfiguration.getEndpoint() + Constants.MARATHON_HEALTH_API;
            Client client = ClientBuilder.newClient();
            client.target(healthCheckEndpoint).request().get(String.class);
            for (Tool eachTool : toolList) {
                int noOfToolsRunning = 0;
                int configuredInstances = 0;
                try {
                    ToolManifest toolManifest = toolUtil.buildToolManifestRecord(eachTool);
                    String appId = toolManifest.getId();
                    GetAppResponse getAppResponse = marathonClientManager.getApp(appId);
                    noOfToolsRunning = getRunningInstances(getAppResponse);
                    configuredInstances = getConfiguredInstances(toolManifest);
                    updateToolStatus(noOfToolsRunning, configuredInstances, eachTool);
                    if (noOfToolsRunning == 0) marathonClientManager.createApp(toolManifest);
                    if (noOfToolsRunning != 0 && noOfToolsRunning < configuredInstances) {
                        int scaleInstances = configuredInstances - noOfToolsRunning;
                        toolManifest.setInstances(scaleInstances);
                        marathonClientManager.updateApp(toolManifest);
                    }
                    reDeployTool(eachTool,appId);
                } catch (MarathonException me) {
                    updateToolStatus(noOfToolsRunning, configuredInstances, eachTool);
                    log.error("MarathonException While checking the tool health check", me.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Tool health check error", e.getMessage());
        } catch (Throwable th) {
            log.error("Tool health check error", th);
        }
    }

    private void updateToolStatus(int runningInstances, int configuredInstances, Tool tool) {
        String currentToolStatus = tool.getStatus();
        if (runningInstances == configuredInstances) {
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
        return toolManifest.getInstances();
    }

    //redeploy tool if session not present with jch
    private void reDeployTool(Tool tool,String appId) throws MarathonException {
        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
        long milliseconds = timestamp.getTime() - tool.getUpdatedAt().getTime();
        int seconds = (int) milliseconds / 1000;
        long differenceHours = seconds / 3600;
        List<ToolInstance> toolInstanceList = toolInstanceDAO.getByToolId(tool.getId());
        if(toolInstanceList.size() == 0 && differenceHours > 1) {
            log.info("Session not hold with jackhammer for tool id {} {} ",tool.getId(),appId);
            marathonClientManager.deleteApp(appId);
        }
    }
}
