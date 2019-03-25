package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Task;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Api
@Path("/api/v1/app/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TasksController extends BaseController {
    @POST
    @Path("/list")
    public Response getAll(@Valid Task task,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            task.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.TASK_SERVICE).getAllRecords(task);
        } catch (Exception e){
            log.error("Error while getting tasks =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TASK_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting tasks =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Task task) {
        try {
            log.debug("Creating Task with information: {}", task);
            return this.getHandlerFactory().getHandler(Handler.TASK_SERVICE).createRecord(task);
        } catch (Exception e){
            log.error("Error while creating task =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Task task) {
        try {
            log.debug("Updating task with information: {}", task);
            return this.getHandlerFactory().getHandler(Handler.TASK_SERVICE).updateRecord(task);

        } catch (Exception e) {
            log.error("Error while updating task =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TASK_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting task =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
