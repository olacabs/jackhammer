package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Dashboard;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class DashboardResponseBuilder extends AbstractResponseBuilder<Dashboard> {

    @Override
    public Response buildFetchRecordResponse(Dashboard dashboard) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(dashboard).build();
    }
}
