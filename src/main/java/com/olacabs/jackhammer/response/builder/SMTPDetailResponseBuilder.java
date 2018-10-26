package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.SMTPDetail;

import javax.ws.rs.core.Response;

public class SMTPDetailResponseBuilder extends AbstractResponseBuilder<SMTPDetail> {
    public Response buildFetchRecordResponse(SMTPDetail smtpDetail) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(smtpDetail).build();
    }
}
