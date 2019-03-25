package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Comment;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class CommentsController extends BaseController {
    @POST
    @Path("/list")
    public Response getRepos(Comment comment) {
        try {
            return this.getHandlerFactory().getHandler(Handler.COMMENT_SERVICE).getAllRecords(comment);
        } catch (Exception e){
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.COMMENT_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Comment comment, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            log.debug("Creating Role with information: {}", comment);
            comment.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.COMMENT_SERVICE).createRecord(comment);
        } catch (Exception e) {
            log.error("Error while creating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Comment comment) {
        try {
            log.debug("Updating repo with information: {}", comment);
            return this.getHandlerFactory().getHandler(Handler.COMMENT_SERVICE).updateRecord(comment);
        } catch (Exception e) {
            log.error("Error while updating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.COMMENT_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
