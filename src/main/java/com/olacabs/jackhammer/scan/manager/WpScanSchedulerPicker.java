package com.olacabs.jackhammer.scan.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.db.ScanTypeDAO;
import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.models.ScanType;
import com.olacabs.jackhammer.utilities.ScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WpScanSchedulerPicker implements Runnable {

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    ScanUtil scanUtil;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    public void run() {
        ScanType scanType = scanTypeDAO.getWpScanType();
        List<Scan> scanList = scanDAO.getWordpressScans(scanType.getId());
        log.info("Total scheduled scans count ..{} {} ", scanList.size());
        for (Scan scan : scanList) {
            scanUtil.startScan(scan);
        }
    }
}
