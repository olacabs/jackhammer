package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Permission;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class PermissionsController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid Permission permission) {
        try {
            return this.getHandlerFactory().getHandler(Handler.PERMISSION_SERVICE).getAllRecords(permission);
        } catch (Exception e){
            log.error("Error while getting permissions =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.PERMISSION_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting permission =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Permission permission) {
        try {
            log.debug("Creating permission with information: {}", permission);
            return this.getHandlerFactory().getHandler(Handler.PERMISSION_SERVICE).createRecord(permission);
        } catch (Exception e){
            log.error("Error while creating permission =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Permission permission) {
       try {
           log.debug("Updating Permission with information: {}", permission);
           return this.getHandlerFactory().getHandler(Handler.PERMISSION_SERVICE).updateRecord(permission);

       } catch (Exception e) {
           log.error("Error while updating permission =>" , e);
           return this.getExceptionHandler().handle(e);
       }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.PERMISSION_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting permission =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
