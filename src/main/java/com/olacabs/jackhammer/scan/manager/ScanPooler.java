package com.olacabs.jackhammer.scan.manager;

import com.google.inject.Inject;


import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.db.ScanToolDAO;
import com.olacabs.jackhammer.db.ScanTypeDAO;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.ScanTool;
import com.olacabs.jackhammer.models.ToolInstance;
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
    MarathonClientManager marathonClientManager;

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    public void start() throws Exception {

        //setting thread pool
        int threadPoolSize = jackhammerConfiguration.getScanMangerConfiguration().getThreadPoolSize();
        int initialDelay = jackhammerConfiguration.getScanMangerConfiguration().getInitialDelay();
        int period = jackhammerConfiguration.getScanMangerConfiguration().getPeriod();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize);
        executor.scheduleAtFixedRate(scanTask, initialDelay, period, TimeUnit.SECONDS);
    }

    public void stop() throws Exception {
        // delete current running containers from db and make scan running scans as failed
        List<ToolInstance> toolInstanceList = toolInstanceDAO.getAll();
        for (ToolInstance toolInstance : toolInstanceList) {
            List<ScanTool> scanToolList = scanToolDAO.getProgressScanTools(toolInstance.getId());
            scanToolDAO.setToolInstanceScanStatusToQueue(toolInstance.getId(), Constants.SCAN_QUEUED_STATUS);
            for (ScanTool scanTool : scanToolList) {
                scanDAO.updateScanStatusToQueue(Constants.SCAN_QUEUED_STATUS, scanTool.getScanId());
            }
        }
        toolInstanceDAO.deleteAll();
    }
}
