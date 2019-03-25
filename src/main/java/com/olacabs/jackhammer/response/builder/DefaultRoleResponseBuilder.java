package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.DefaultRole;

import javax.ws.rs.core.Response;

public class DefaultRoleResponseBuilder extends AbstractResponseBuilder<DefaultRole> {
    public Response buildFetchRecordResponse(DefaultRole defaultRole) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(defaultRole).build();
    }
}
