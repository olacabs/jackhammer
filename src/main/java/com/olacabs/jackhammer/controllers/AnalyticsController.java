package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Analytics;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/app/analytics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AnalyticsController extends BaseController {

    @POST
    @Path("/view")
    public Response getAll(@Valid Analytics analytics,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            analytics.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.ANALYTICS_SERVICE).getAllRecords(analytics);
        } catch (Exception e){
            log.error("Error while getting analytics =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
