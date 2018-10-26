package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Group;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/app/applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ApplicationsController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid Group group) {
        try {
            return this.getHandlerFactory().getHandler(Handler.APPLICATION_SERVICE).getAllRecords(group);
        } catch (Exception e){
            log.error("Error while getting roles =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.APPLICATION_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting roles =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Group group) {
        try {
            log.debug("Creating Group with information: {}", group);
            return this.getHandlerFactory().getHandler(Handler.APPLICATION_SERVICE).createRecord(group);
        } catch (Exception e){
            log.error("Error while creating group =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Group group) {
        try {
            log.debug("Updating group with information: {}", group);
            return this.getHandlerFactory().getHandler(Handler.APPLICATION_SERVICE).updateRecord(group);

        } catch (Exception e) {
            log.error("Error while updating group =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.APPLICATION_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting group =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
