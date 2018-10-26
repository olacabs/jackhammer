package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.Git;

import javax.ws.rs.core.Response;

public class GitResponseBuilder extends AbstractResponseBuilder<Git> {
    public Response buildFetchRecordResponse(Git git) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(git).build();
    }
}
