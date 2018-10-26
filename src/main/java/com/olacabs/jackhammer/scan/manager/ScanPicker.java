package com.olacabs.jackhammer.scan.manager;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.db.ScanDAO;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.utilities.ScanUtil;

@Slf4j
public class ScanPicker implements Runnable {

    @Inject
    ScanUtil scanUtil;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    public void run() {
        List<Scan> scanList = scanDAO.getQueuedScans();
        log.info("Pending scans count ==>{} {} ", scanList.size());
        for (Scan scan : scanList) {
            scanUtil.startScan(scan);
        }
    }
}
