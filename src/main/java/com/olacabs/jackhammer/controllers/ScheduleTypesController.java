package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.ScheduleType;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Api
@Path("/api/v1/app/schedule_types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ScheduleTypesController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid ScheduleType scheduleType,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            scheduleType.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.SCHEDULE_TYPE_SERVICE).getAllRecords(scheduleType);
        } catch (Exception e){
            log.error("Error while getting schedule type services =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SCHEDULE_TYPE_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting schedule type services =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid ScheduleType scheduleType) {
        try {
            log.debug("Creating Role with information: {}", scheduleType);
            return this.getHandlerFactory().getHandler(Handler.SCHEDULE_TYPE_SERVICE).createRecord(scheduleType);
        } catch (Exception e){
            log.error("Error while creating schedule type service =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid ScheduleType scheduleType) {
        try {
            log.debug("Updating repo with information: {}", scheduleType);
            return this.getHandlerFactory().getHandler(Handler.SCHEDULE_TYPE_SERVICE).updateRecord(scheduleType);

        } catch (Exception e) {
            log.error("Error while updating schedule type service =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SCHEDULE_TYPE_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting schedule type service =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
