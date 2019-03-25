package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.HardcodeSecret;

import javax.ws.rs.core.Response;

public class HardcodeSecretResponseBuilder extends AbstractResponseBuilder<HardcodeSecret> {
    public Response buildFetchRecordResponse(HardcodeSecret hardcodeSecret) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(hardcodeSecret).build();
    }
}
