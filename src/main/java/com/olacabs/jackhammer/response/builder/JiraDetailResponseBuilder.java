package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.JiraDetail;

import javax.ws.rs.core.Response;

public class JiraDetailResponseBuilder extends AbstractResponseBuilder<JiraDetail> {
    public Response buildFetchRecordResponse(JiraDetail jiraDetail) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(jiraDetail).build();
    }
}
