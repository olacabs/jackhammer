package com.olacabs.jackhammer.controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.validation.Valid;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Role;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Path("/api/v1/app/roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class RolesController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid Role role,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            role.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.ROLE_SERVICE).getAllRecords(role);
        } catch (Exception e){
            log.error("Error while getting roles =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.ROLE_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting roles =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Role role) {
        try {
            log.debug("Creating Role with information: {}", role);
            return this.getHandlerFactory().getHandler(Handler.ROLE_SERVICE).createRecord(role);
        } catch (Exception e){
            log.error("Error while creating role =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Role role) {
        try {
            log.debug("Updating role with information: {}", role);
            return this.getHandlerFactory().getHandler(Handler.ROLE_SERVICE).updateRecord(role);

        } catch (Exception e) {
            log.error("Error while updating role =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.ROLE_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting role =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
