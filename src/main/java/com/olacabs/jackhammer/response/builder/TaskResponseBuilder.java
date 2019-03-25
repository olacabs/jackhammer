package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.Task;

import javax.ws.rs.core.Response;

public class TaskResponseBuilder extends AbstractResponseBuilder<Task> {
    public Response buildFetchRecordResponse(Task task) throws OperationFailedException {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(task).build();
    }
}
