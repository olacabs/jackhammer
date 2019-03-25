package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.SeverityLevel;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/api/v1/app/severities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeverityLevelsController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid SeverityLevel severityLevel,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            severityLevel.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.SEVERITY_LEVEL_SERVICE).getAllRecords(severityLevel);
        } catch (Exception e){
            log.error("Error while getting roles =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SEVERITY_LEVEL_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting  severity levels =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }


    @POST
    public Response create(@Valid SeverityLevel severityLevel, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            severityLevel.setUserToken(userToken);
            log.debug("Creating Role with information: {}", severityLevel);
            return this.getHandlerFactory().getHandler(Handler.SEVERITY_LEVEL_SERVICE).createRecord(severityLevel);
        } catch (Exception e){
            log.error("Error while creating severity levels details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid SeverityLevel severityLevel) {
        try {
            log.debug("Updating severity levels details with information: {}", severityLevel);
            return this.getHandlerFactory().getHandler(Handler.SEVERITY_LEVEL_SERVICE).updateRecord(severityLevel);

        } catch (Exception e) {
            log.error("Error while updating severity levels details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
