package com.olacabs.jackhammer.scan.manager;

import com.google.inject.Inject;


import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.models.ScanTool;
import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.models.ToolManifest;
import com.olacabs.jackhammer.utilities.DockerUtil;
import com.olacabs.jackhammer.utilities.ToolUtil;
import io.dropwizard.lifecycle.Managed;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

import com.olacabs.jackhammer.tool.interfaces.container.manager.MarathonClientManager;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;


@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ScanPooler implements Managed {

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    ScanPicker scanTask;

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    ToolUtil toolUtil;

    @Inject
    DockerUtil dockerUtil;

    @Inject
    MarathonClientManager marathonClientManager;

    public void start() {
        try {

            toolInstanceDAO.deleteAll();
            scanDAO.addProgressingScansToQueue();
            //setting thread pool
            int threadPoolSize = jackhammerConfiguration.getScanMangerConfiguration().getThreadPoolSize();
            int initialDelay = jackhammerConfiguration.getScanMangerConfiguration().getInitialDelay();
            int period = jackhammerConfiguration.getScanMangerConfiguration().getPeriod();
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize);
            executor.scheduleAtFixedRate(scanTask, initialDelay, period, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error while running scanTask..", e);
        } catch (Throwable th) {
            log.error("Error while running scanTask..", th);
        }
    }

    public void stop() {
        try {
            log.info("jackhammer server  going down.....");
            // delete current running containers from db and make running scans as failed
            List<ToolInstance> toolInstanceList = toolInstanceDAO.getAll();

            for (ToolInstance toolInstance : toolInstanceList) {
                List<ScanTool> scanToolList = scanToolDAO.getProgressScanTools(toolInstance.getId());
                scanToolDAO.setToolInstanceScanStatusToQueue(toolInstance.getId(), Constants.SCAN_QUEUED_STATUS);
                for (ScanTool scanTool : scanToolList) {
                    scanDAO.updateScanStatusToQueue(Constants.SCAN_QUEUED_STATUS, scanTool.getScanId());
                }
            }

            List<Tool> tools = toolDAO.getAll();
            Boolean enabledMarathon = Boolean.valueOf(System.getenv(Constants.ENABLED_MARATHON));
//            deleting tools from marathon
            for (Tool tool : tools) {
                try {
//                    toolInstanceDAO.deleteByToolId(tool.getId());
                    ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
                    String appId = toolManifest.getId();
                    if (enabledMarathon) {
                        marathonClientManager.deleteApp(appId);
                    } else {
                        dockerUtil.stopContainers(toolManifest.getContainer().getDocker().getImage());
                    }
                } catch (Exception e) {
                    log.error("Error while updating tool instance...", e);
                }
            }

            log.info("Deleting all records from tool instances table");
            toolInstanceDAO.deleteAll();
        } catch (Exception e) {
            log.error("Error while updating tool instance...", e);
        } catch (Throwable th) {
            log.error("Error while updating tool instance...", th);
        }
    }
}
