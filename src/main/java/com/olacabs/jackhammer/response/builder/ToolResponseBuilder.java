package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.HttpResponseCodes;
import com.olacabs.jackhammer.models.Tool;

import javax.ws.rs.core.Response;

public class ToolResponseBuilder extends  AbstractResponseBuilder<Tool> {

    @Override
    public Response buildFetchRecordResponse(Tool tool) {
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).entity(tool).build();
    }
}
