package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.SeverityLevel;

import javax.ws.rs.core.Response;

public class SeverityLevelResponseBuilder extends AbstractResponseBuilder<SeverityLevel> {
    public Response buildFetchRecordResponse(SeverityLevel severityLevel) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(severityLevel).build();
    }
}
