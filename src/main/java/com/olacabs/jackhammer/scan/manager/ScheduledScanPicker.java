package com.olacabs.jackhammer.scan.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.db.ScanToolDAO;
import com.olacabs.jackhammer.db.ScheduleTypeDAO;
import com.olacabs.jackhammer.db.ToolInstanceDAO;
import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.models.ScanTool;
import com.olacabs.jackhammer.models.ScheduleType;
import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.utilities.ScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class ScheduledScanPicker implements Runnable {

    @Inject
    ScanUtil scanUtil;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.TOOL_INSTANCE_DAO)
    ToolInstanceDAO toolInstanceDAO;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.SCHEDULE_TYPE_DAO)
    ScheduleTypeDAO scheduleTypeDAO;


    public void run() {
        List<Scan> scanList = scanDAO.getScheduledScans();
        log.info("Total scheduled scans count ..{} {} ", scanList.size());
        for (Scan scan : scanList) {
            if (canScan(scan)) {
                log.info("schedule scan picked with....{}..{}", scan.getId());
                scanUtil.startScan(scan);
            }
        }

        // verifying scan tool status, add to queue if it is more than a day
        List<ScanTool> scanToolList = scanToolDAO.getAllProgressScans();
        for (ScanTool scanTool : scanToolList) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
            if (sqlDate.compareTo(scanTool.getUpdatedAt()) >= 0) {
                scanToolDAO.setToolInstanceScanStatusToQueue(scanTool.getToolInstanceId(), Constants.SCAN_QUEUED_STATUS);
                ToolInstance toolInstance = toolInstanceDAO.get(scanTool.getToolInstanceId());
                if (toolInstance.getCurrentRunningScans() >= 1)
                    toolInstanceDAO.decreaseRunningScans(scanTool.getToolInstanceId());
                Scan scan = scanDAO.get(scanTool.getScanId());
                scan.setStatus(Constants.SCAN_QUEUED_STATUS);
                scanDAO.updateScanStatus(scan);
            }
        }
    }

    Boolean canScan(Scan scan) {
        ScheduleType scheduleType = scheduleTypeDAO.get(scan.getScheduleTypeId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -scheduleType.getDays());
        java.util.Date scheduledDate = calendar.getTime();
        Date dbScheduledDate = new Date(scheduledDate.getTime());
        return dbScheduledDate.compareTo(scan.getLastRunDate()) > 0;
    }
}
