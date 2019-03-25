package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.HttpResponseCodes;
import com.olacabs.jackhammer.models.ScheduleType;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

public class ScheduleTypeResponseBuilder extends AbstractResponseBuilder<ScheduleType> {

    @Override
    public Response buildFetchRecordResponse(ScheduleType scheduleType) {
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).entity(scheduleType).build();
    }
}
