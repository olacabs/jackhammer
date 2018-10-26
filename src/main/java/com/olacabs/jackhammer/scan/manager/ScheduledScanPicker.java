package com.olacabs.jackhammer.scan.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.db.ScheduleTypeDAO;
import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.models.ScheduleType;
import com.olacabs.jackhammer.utilities.ScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class ScheduledScanPicker implements Runnable {

    @Inject
    ScanUtil scanUtil;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.SCHEDULE_TYPE_DAO)
    ScheduleTypeDAO scheduleTypeDAO;


    public void run() {
        List<Scan> scanList = scanDAO.getScheduledScans();
        log.info("Total scheduled scans count ..{} {} " , scanList.size());
        for (Scan scan : scanList) {
            if (canScan(scan)) {
                log.info("schedule scan picked with....{}..{}",scan.getId());
                scanUtil.startScan(scan);
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
