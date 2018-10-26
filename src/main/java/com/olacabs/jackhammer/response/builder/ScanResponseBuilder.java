package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Scan;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

public class ScanResponseBuilder extends AbstractResponseBuilder<Scan> {

    @Override
    public Response buildFetchRecordResponse(Scan scan) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(scan).build();
    }
}
