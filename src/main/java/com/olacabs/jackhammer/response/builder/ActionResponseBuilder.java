package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.Action;

import javax.ws.rs.core.Response;

public class ActionResponseBuilder extends AbstractResponseBuilder<Action> {
    public Response buildFetchRecordResponse(Action action) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(action).build();
    }
}
