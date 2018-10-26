package com.olacabs.jackhammer.tool.interfaces.request;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScanDAO;
import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.tool.interfaces.sdk.bridge.SdkCommunicator;


public class ScanRequest {

    @Inject
    SdkCommunicator sdkCommunicator;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    public void changeScanStatus(Scan scan) {
        scan.setStatus(Constants.SCAN_PROGRESS_STATUS);
        scanDAO.updateScanStatus(scan);
    }

}
