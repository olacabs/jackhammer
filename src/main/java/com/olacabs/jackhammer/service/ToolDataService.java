package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.db.ScanToolDAO;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.tool.interfaces.container.manager.MarathonClientManager;
import com.olacabs.jackhammer.utilities.DockerUtil;
import com.olacabs.jackhammer.utilities.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.MarathonClient;
import mesosphere.marathon.client.utils.MarathonException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ToolDAO;


@Slf4j
public class ToolDataService extends AbstractDataService<Tool> {

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    ToolUtil toolUtil;

    @Inject
    MarathonClientManager marathonClientManager;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    DockerUtil dockerUtil;

    @Override
    public PagedResponse<Tool> getAllRecords(Tool tool) {
        if (tool.getSearchTerm() == null) {
            paginationRecords.setItems(toolDAO.getAll(tool, tool.getOrderBy(), tool.getSortDirection()));
            paginationRecords.setTotal(toolDAO.totalCount(tool));
        } else {
            paginationRecords.setItems(toolDAO.getSearchResults(tool, tool.getOrderBy(), tool.getSortDirection()));
            paginationRecords.setTotal(toolDAO.totalSearchCount(tool));
        }
        setCRUDPermissions(paginationRecords, tool, getCurrentTask(Constants.TOOLS, tool.getOwnerTypeId()));
        return paginationRecords;
    }

    @Override
    public Tool fetchRecordByname(Tool tool) {
        return toolDAO.findToolByName(tool.getName());
    }

    @Override
    public Tool fetchRecordById(long id) {
        return toolDAO.get(id);
    }

    @Override
    public Tool createRecord(Tool tool) {
        setManifest(tool);
        long id = toolDAO.insert(tool);
        return toolDAO.get(id);
    }

    @Override
    public void updateRecord(Tool tool) {
        setManifest(tool);
        ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
        marathonClientManager.updateApp(toolManifest);
        toolDAO.update(tool);
    }

    @Override
    public void deleteRecord(long id) {
        Tool tool = fetchRecordById(id);
        ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
        scanToolDAO.deleteToolScans(id);
//        toolInstanceDAO.deleteByToolId(id);
        Boolean enabledMarathon = Boolean.valueOf(System.getenv(Constants.ENABLED_MARATHON));
        if (toolManifest != null) {
            String appId = toolManifest.getId();
            if (enabledMarathon) {
                try {
                    marathonClientManager.deleteApp(appId);
                } catch (MarathonException me) {
                    log.error("Error while deleting app from marathon  {} ", me);
                }
            } else {
                killAllToolContainers(toolManifest);
            }
        }
        toolDAO.delete(id);
    }

    private void setManifest(Tool tool) {
        String manifestJson = "";
        try {
            manifestJson = IOUtils.toString(tool.getUploadedInputStream(), StandardCharsets.UTF_8);
        } catch (IOException io) {
            log.error("Error while dumping json to string");
        }
        tool.setManifestJson(manifestJson);
        tool.setStatus("Deploying started...");
    }

    private void killAllToolContainers(final ToolManifest toolManifest) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                dockerUtil.stopContainers(toolManifest.getContainer().getDocker().getImage());
            }
        };
        int delay = 0;
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
        scheduler.shutdown();
    }
}
