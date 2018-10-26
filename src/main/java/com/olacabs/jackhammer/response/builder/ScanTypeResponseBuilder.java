package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.HttpResponseCodes;
import com.olacabs.jackhammer.models.ScanType;
import com.olacabs.jackhammer.models.Tool;

import javax.ws.rs.core.Response;

public class ScanTypeResponseBuilder extends  AbstractResponseBuilder<ScanType> {

    @Override
    public Response buildFetchRecordResponse(ScanType scanType) {
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).entity(scanType).build();
    }
}
