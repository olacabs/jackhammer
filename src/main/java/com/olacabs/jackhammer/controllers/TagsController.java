package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Tag;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class TagsController extends BaseController {
    @POST
    @Path("/list")
    public Response getTags(Tag tag) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TAG_SERVICE).getAllRecords(tag);
        } catch (Exception e){
            log.error("Error while getting tags =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TAG_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting tags =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Tag tag, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            tag.setUserToken(userToken);
            log.debug("Creating Role with information: {}", tag);
            return this.getHandlerFactory().getHandler(Handler.TAG_SERVICE).createRecord(tag);
        } catch (Exception e){
            log.error("Error while creating tag =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Tag tag) {
        try {
            log.debug("Updating tag with information: {}", tag);
            return this.getHandlerFactory().getHandler(Handler.TAG_SERVICE).updateRecord(tag);

        } catch (Exception e) {
            log.error("Error while updating tag =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TAG_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting tag =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
