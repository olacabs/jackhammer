package com.olacabs.jackhammer.tool.interfaces.request;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.models.Scan;


public class ScanRequest {


    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    public void changeScanStatus(Scan scan,String status) {
        scan.setStatus(Constants.SCAN_PROGRESS_STATUS);
        scan.setStatus(status);
        scanDAO.updateScanStatus(scan);
    }

}
