package com.olacabs.jackhammer.tool.interfaces.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import javax.websocket.Session;

import java.io.IOException;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.db.ScanToolDAO;

@Slf4j
public class ToolResponse {

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    public ToolInstance addToolInstance(String response, Session session) {
        ToolInstance toolInstance = buildToolInstance(response, session);
//        toolInstanceDAO.deleteByToolId(toolInstance.getToolId());
        long id = toolInstanceDAO.insert(toolInstance);
        ToolInstance insertedToolInstance = toolInstanceDAO.get(id);
        return insertedToolInstance;
    }

    public Boolean isToolResponse(String objectResponse) {
        Boolean isToolInfoResponse = false;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode scanNode = mapper.readTree(objectResponse);
            String responseInstance = mapper.convertValue(scanNode.get(Constants.RESPONSE_INSTANCE), String.class);
            if (StringUtils.equals(responseInstance, Constants.TOOL)) isToolInfoResponse = true;
        } catch (IOException io) {
            log.error("Error while reading tool response", io);
        }
        return isToolInfoResponse;
    }

    public void updateScanToolStatus(long scanId, long toolId, long toolInstanceId) {
        scanToolDAO.assignedToolInstance(scanId, toolId, Constants.SCAN_PROGRESS_STATUS, toolInstanceId);
        toolInstanceDAO.increaseRunningScans(toolInstanceId);
    }

    public void decreaseRunningScans(long toolInstanceId) {
        toolInstanceDAO.decreaseRunningScans(toolInstanceId);
    }

    public void deleteToolInstance(long toolInstanceId) {
        scanToolDAO.setToolInstanceScanStatusToQueue(toolInstanceId, Constants.SCAN_QUEUED_STATUS);
        toolInstanceDAO.deleteById(toolInstanceId);
    }

    public void deleteToolInstanceBySession(String sessionId) {
        ToolInstance toolInstance = toolInstanceDAO.getBySessionId(sessionId);
        deleteToolInstance(toolInstance.getId());
    }

    private ToolInstance buildToolInstance(String response, Session session) {
        ObjectMapper mapper = new ObjectMapper();
        ToolInstance toolInstance = new ToolInstance();
        try {
            JsonNode toolNode = mapper.readTree(response);
            String supportedPlatform = mapper.convertValue(toolNode.get(Constants.SUPPORTED_PLATFORM), String.class);
            String hostname = mapper.convertValue(toolNode.get(Constants.HOSTNAME), String.class);
            String containerPort = mapper.convertValue(toolNode.get(Constants.PORT), String.class);
            long toolId = mapper.convertValue(toolNode.get(Constants.INSTANCE_TOOL_ID), Long.class);
            long maxAllowedScans = mapper.convertValue(toolNode.get(Constants.MAX_ALLOWED_SCANS), Long.class);
            int port = containerPort == null ? 0 : Integer.valueOf(containerPort);
            toolInstance.setSessionId(session.getId());
            toolInstance.setToolId(toolId);
            toolInstance.setPlatform(supportedPlatform);
            toolInstance.setStatus(Constants.HEALTHY);
            toolInstance.setMaxAllowedScans(maxAllowedScans);
            toolInstance.setContainerId(hostname);
            toolInstance.setPort(port);
        } catch (IOException io) {
            log.error("IOException while reading client response", io);
        } catch (Exception e) {
            log.error("Exception while reading client response", e);
        }
        return toolInstance;
    }
}
