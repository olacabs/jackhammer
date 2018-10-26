package com.olacabs.jackhammer.controllers;


import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Filter;
import com.olacabs.jackhammer.models.Finding;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/filters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class FiltersController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid Filter filter,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            filter.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.FILTER_SERVICE).getAllRecords(filter);
        } catch (Exception e) {
            log.error("Error while getting filter results =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
