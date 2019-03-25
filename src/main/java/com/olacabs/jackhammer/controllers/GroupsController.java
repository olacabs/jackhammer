package com.olacabs.jackhammer.controllers;


import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.Repo;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
@Slf4j
public class GroupsController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid Group group,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            group.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.GROUP_SERVICE).getAllRecords(group);
        } catch (Exception e){
            log.error("Error while getting groups =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.GROUP_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting groups =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Group group) {
        try {
            log.debug("Creating Group with information: {}", group);
            return this.getHandlerFactory().getHandler(Handler.GROUP_SERVICE).createRecord(group);
        } catch (Exception e){
            log.error("Error while creating group =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Group group) {
        try {
            log.debug("Updating Group with information: {}", group);
            return this.getHandlerFactory().getHandler(Handler.GROUP_SERVICE).updateRecord(group);

        } catch (Exception e) {
            log.error("Error while updating Group =>" , e);
            return this.getExceptionHandler().handle(e);
        }

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.GROUP_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting Group =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
