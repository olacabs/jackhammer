package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Finding;

import javax.ws.rs.core.Response;

public class FindingResponseBuilder extends AbstractResponseBuilder<Finding> {

    @Override
    public Response buildFetchRecordResponse(Finding finding) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(finding).build();
    }
}
