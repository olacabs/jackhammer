package com.olacabs.jackhammer.tool.interfaces.container.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.db.ToolDAO;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.models.ToolManifest;
import com.olacabs.jackhammer.utilities.DockerUtil;
import com.olacabs.jackhammer.utilities.ToolUtil;

import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.model.v2.GetAppTasksResponse;
import mesosphere.marathon.client.model.v2.Task;
import mesosphere.marathon.client.utils.MarathonException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class AutoScalingTool implements Runnable {

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    ToolUtil toolUtil;

    @Inject
    MarathonClientManager marathonClientManager;

    @Inject
    DockerUtil dockerUtil;


    public void run() {
        try {
            List<Tool> toolList = toolDAO.getAll();
            Boolean enabledMarathon = Boolean.valueOf(System.getenv(Constants.ENABLED_MARATHON));
            if (enabledMarathon) {
                containersHealthCheckWithMarathon(toolList);
            } else {
                containersHealthCheckWithNoMarathon(toolList);
            }
        } catch (Throwable th) {
            log.error("Error while doing auto scaling...", th);
        }
    }


    private void containersHealthCheckWithMarathon(List<Tool> toolList) {
        for (Tool tool : toolList) {
            try {
                ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
                GetAppResponse getAppResponse = marathonClientManager.getApp(toolManifest.getId());
                String platform = toolManifest.getEnv().get(Constants.TOOL_SUPPORTED_PLATFORM);
                int maxAllowedScans = Integer.valueOf(toolManifest.getEnv().get(Constants.TOOL_MAX_ALLOWED_SCANS)) * 3;
                int queuedScansCount = scanDAO.getToolQueuedScansCount(platform.toLowerCase());
                int maxInstances = toolManifest.getMaxInstances();
                int minimumInstances = toolManifest.getMinInstances();
                int runningInstances = getAppResponse == null ? 0 : getAppResponse.getApp().getTasksRunning();
                int expectedRunningInstances = queuedScansCount / maxAllowedScans;
                log.info("runningInstances=> {},expectedRunningInstances=>{},queuedScansCount=>{}," +
                                "maxInstances,=>{},minimumInstances=>{}", runningInstances, expectedRunningInstances,
                        queuedScansCount, maxInstances, minimumInstances);
                if (queuedScansCount != 0
                        && (maxInstances == runningInstances
                        || queuedScansCount < maxAllowedScans
                        || runningInstances == expectedRunningInstances)) continue;
                if (maxInstances < expectedRunningInstances) {
                    toolManifest.setInitialInstances(maxInstances);
                    marathonClientManager.updateApp(toolManifest);
                } else if (runningInstances < expectedRunningInstances) {
                    toolManifest.setInitialInstances(expectedRunningInstances);
                    marathonClientManager.updateApp(toolManifest);
                } else if (runningInstances > expectedRunningInstances && expectedRunningInstances != 0) {
                    int stopTasks = runningInstances - expectedRunningInstances;
                    stopMarathonToolInstances(stopTasks, tool);
                } else if (expectedRunningInstances == 0
                        && minimumInstances != 0
                        && runningInstances > minimumInstances) {
                    int stopTasks = runningInstances - expectedRunningInstances;
                    stopMarathonToolInstances(stopTasks, tool);
                } else if (minimumInstances == 0) {
                    marathonClientManager.deleteApp(toolManifest.getId());
                }
            } catch (MarathonException me) {
                log.error("MarathonException while updating the tool instances ..{}..{}", tool.getId(), tool.getName());
            }
        }
    }

    private void containersHealthCheckWithNoMarathon(List<Tool> toolList) {
        Map<String, Integer> containerImages = dockerUtil.getContainerImages();
        for (Tool tool : toolList) {
            try {
                ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
                String imageId = toolManifest.getContainer().getDocker().getImage();
                String platform = toolManifest.getEnv().get(Constants.TOOL_SUPPORTED_PLATFORM);
                String image = toolManifest.getContainer().getDocker().getImage();
                int maxAllowedScans = Integer.valueOf(toolManifest.getEnv().get(Constants.TOOL_MAX_ALLOWED_SCANS)) * 3;
                int queuedScansCount = scanDAO.getToolQueuedScansCount(platform.toLowerCase());
                int maxInstances = toolManifest.getMaxInstances();
                int minimumInstances = toolManifest.getMinInstances();
                int runningInstances = containerImages.get(image) == null ? 0 : containerImages.get(image);
                int expectedRunningInstances = queuedScansCount / maxAllowedScans;

                log.info("runningInstances=> {},expectedRunningInstances=>{},queuedScansCount=>{}," +
                                "maxInstances,=>{},minimumInstances=>{}", runningInstances, expectedRunningInstances,
                        queuedScansCount, maxInstances, minimumInstances);

                if (queuedScansCount != 0
                        && (maxInstances == runningInstances
                        || queuedScansCount < maxAllowedScans
                        || runningInstances == expectedRunningInstances)) continue;
                if (maxInstances < expectedRunningInstances) {
                    toolManifest.setInitialInstances(maxInstances);
                    dockerUtil.startInstances(imageId, toolManifest);
                } else if (runningInstances < expectedRunningInstances && expectedRunningInstances != 0) {
                    toolManifest.setInitialInstances(expectedRunningInstances);
                    dockerUtil.startInstances(imageId, toolManifest);
                } else if (runningInstances > expectedRunningInstances && expectedRunningInstances != 0) {
                    int stopContainers = runningInstances - expectedRunningInstances;
                    stopNotMarathonToolInstances(stopContainers, tool);
                } else if (expectedRunningInstances == 0 && minimumInstances != 0 && runningInstances > minimumInstances) {
                    int stopContainers = runningInstances - minimumInstances;
                    stopNotMarathonToolInstances(stopContainers, tool);
                } else if (minimumInstances == 0) {
                    dockerUtil.stopContainers(imageId);
                }
            } catch (Exception e) {
                log.error("Exception while updating the tool instances ..{}..{}", tool.getId(), tool.getName());
            }
        }
    }

    private void stopNotMarathonToolInstances(int stopContainers, Tool tool) {
        List<ToolInstance> toolInstanceList = toolInstanceDAO.getByToolId(tool.getId());
        int count = 0;
        for (ToolInstance toolInstance : toolInstanceList) {
            if (count == stopContainers) break;
            ToolInstance dbToolInstance = toolInstanceDAO.get(toolInstance.getId());
            if (dbToolInstance.getCurrentRunningScans() == 0) {
                log.info("stopping container with id...{}", toolInstance.getContainerId());
                dockerUtil.stopContainer(toolInstance.getContainerId());
                count += 1;
            }
        }
    }

    private void stopMarathonToolInstances(int stopContainers, Tool tool) {
        List<ToolInstance> toolInstanceList = toolInstanceDAO.getByToolId(tool.getId());
        int count = 0;
        ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
        GetAppTasksResponse getAppTasksResponse = marathonClientManager.getAppTasks(toolManifest.getId());
        Collection<Task> tasks = getAppTasksResponse.getTasks();
        for (ToolInstance toolInstance : toolInstanceList) {
            if (count == stopContainers) break;
            ToolInstance dbToolInstance = toolInstanceDAO.get(toolInstance.getId());
            if (dbToolInstance.getCurrentRunningScans() == 0) {
                Boolean killedTask = killTask(toolInstance, tasks);
                if (killedTask) count += 1;
            }

        }
    }

    private Boolean killTask(ToolInstance toolInstance, Collection<Task> tasks) {
        for (Task task : tasks) {
            int taskPort = 0;
            for (Integer port : task.getPorts()) {
                taskPort = port;
                break;
            }
            if (task.getPorts().size() > 0 && taskPort == toolInstance.getPort()) {
                marathonClientManager.deleteAppTask(task.getAppId(), task.getId(), "true");
                log.info("stopping container with id...{}", toolInstance.getContainerId());
                return true;
            }
        }
        return false;
    }
}
