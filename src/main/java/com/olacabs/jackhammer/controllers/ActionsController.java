package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Action;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Api
@Path("/api/v1/app/actions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActionsController extends BaseController {
    @POST
    @Path("/list")
    public Response getAll(@Valid Action action,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            action.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.ACTION_SERVICE).getAllRecords(action);
        } catch (Exception e){
            log.error("Error while getting actions =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.ACTION_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting actions =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Action action) {
        try {
            log.debug("Creating Action with information: {}", action);
            return this.getHandlerFactory().getHandler(Handler.ACTION_SERVICE).createRecord(action);
        } catch (Exception e){
            log.error("Error while creating action =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Action action) {
        try {
            log.debug("Updating action with information: {}", action);
            return this.getHandlerFactory().getHandler(Handler.ACTION_SERVICE).updateRecord(action);

        } catch (Exception e) {
            log.error("Error while updating action =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.ACTION_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting action =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
