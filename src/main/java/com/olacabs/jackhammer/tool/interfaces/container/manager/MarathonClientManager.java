package com.olacabs.jackhammer.tool.interfaces.container.manager;

import com.google.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.MarathonClient;
import mesosphere.marathon.client.model.v2.*;
import mesosphere.marathon.client.utils.MarathonException;

import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.models.MarathonModel;
import com.olacabs.jackhammer.models.ToolManifest;


@Slf4j
public class MarathonClientManager {

    @Inject
    DockerContainer dockerContainer;

    @Inject
    DockerHealthCheck dockerHealthCheck;

    @Inject
    private JackhammerConfiguration jackhammerConfiguration;

    public void createApp(ToolManifest toolManifest) throws MarathonException {
            MarathonModel marathonModel = buildMarathonModel(toolManifest);
            Marathon marathon = getMarathonAgent();
            marathon.createApp(marathonModel);
    }

    public GetAppResponse getApp(String appId) {
        GetAppResponse getAppResponse = null;
        try {
            Marathon marathon = getMarathonAgent();
            getAppResponse = marathon.getApp(appId);
        } catch (MarathonException me) {
            log.error("Tool current Status {} {} " , me.getMessage());
        }
        return getAppResponse;
    }

    public GetAppTasksResponse getAppTasks(String appId) {
        Marathon marathon = getMarathonAgent();
        GetAppTasksResponse getAppTasksResponse = marathon.getAppTasks(appId);
        return getAppTasksResponse;
    }

    public DeleteAppTaskResponse deleteAppTask(String appId,String taskId,String scale) {
        Marathon marathon = getMarathonAgent();
        DeleteAppTaskResponse deleteAppTaskResponse = marathon.deleteAppTask(appId,taskId,scale);
        return deleteAppTaskResponse;
    }

    public void updateApp(ToolManifest toolManifest) {
        MarathonModel marathonModel = buildMarathonModel(toolManifest);
        Marathon marathon = getMarathonAgent();
        marathon.updateApp(marathonModel.getId(),marathonModel);
    }

    public Result deleteApp(String appId) throws MarathonException {
        Marathon marathon = getMarathonAgent();
        return marathon.deleteApp(appId);
    }
    private Marathon  getMarathonAgent() {
        String endpoint = jackhammerConfiguration.getMarathonConfiguration().getEndpoint();
        return MarathonClient.getInstance(endpoint);
    }

    private MarathonModel buildMarathonModel(ToolManifest toolManifest) {
        MarathonModel marathonModel = new MarathonModel();
        marathonModel.setId(toolManifest.getId());
        marathonModel.setCpus(toolManifest.getCpus());
        marathonModel.setMem(toolManifest.getMem());
        marathonModel.setInstances(toolManifest.getInitialInstances());
        marathonModel.setContainer(dockerContainer.getContainer(toolManifest));
        marathonModel.setHealthChecks(dockerHealthCheck.getDockerHealthCheck(toolManifest));
        marathonModel.setEnv(toolManifest.getEnv());
        return marathonModel;
    }
}
