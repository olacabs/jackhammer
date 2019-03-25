package com.olacabs.jackhammer.tool.interfaces.container.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.db.ScanToolDAO;
import com.olacabs.jackhammer.db.ToolDAO;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.ScanTool;
import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.models.ToolManifest;
import com.olacabs.jackhammer.utilities.DockerUtil;
import com.olacabs.jackhammer.utilities.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.utils.MarathonException;

import java.util.List;

@Slf4j
public class HangedToolInstanceCheck implements Runnable {

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    MarathonClientManager marathonClientManager;

    @Inject
    ToolUtil toolUtil;

    @Inject
    DockerUtil dockerUtil;

    public void run() {
        List<ToolInstance> toolInstanceList = toolInstanceDAO.hangInstances();
        for (ToolInstance toolInstance : toolInstanceList) {
            try {
                List<ScanTool> scanToolList = scanToolDAO.getByInstanceId(toolInstance.getId());
                for (ScanTool scanTool : scanToolList) {
                    log.info("Pushing scans to queued for instance id and for scan id ...{}...{}", toolInstance.getId(), scanTool.getScanId());
                    scanToolDAO.pushScanToQueued(toolInstance.getId(), Constants.SCAN_PROGRESS_STATUS, scanTool.getScanId());
                    scanDAO.updateScanStatusToQueue(Constants.SCAN_QUEUED_STATUS, scanTool.getScanId());
                }
                Tool tool = toolDAO.get(toolInstance.getToolId());
                ToolManifest toolManifest = toolUtil.buildToolManifestRecord(tool);
                String appId = toolManifest.getId();
                Boolean enabledMarathon = Boolean.valueOf(System.getenv(Constants.ENABLED_MARATHON));
                if (enabledMarathon) {
                    marathonClientManager.deleteApp(appId);
                } else {
                    dockerUtil.stopContainers(toolManifest.getContainer().getDocker().getImage());
                }
            } catch (MarathonException me) {
                log.error("MarathonException in tool while check tool instance hang state", me);
            } catch (Throwable th) {
                log.error("Throwable in tool while check tool instance hang state", th);
            }
        }
    }
}
