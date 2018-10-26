package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Analytics;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class AnalyticsResponseBuilder extends AbstractResponseBuilder<Analytics> {

    @Override
    public Response buildFetchRecordResponse(Analytics analytics) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(analytics).build();
    }
}
