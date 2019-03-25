package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Git;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/app/git")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class GitController extends BaseController {

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.GIT_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting git =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Git git) {
        try {
            log.debug("Creating Git with information: {}", git);
            return this.getHandlerFactory().getHandler(Handler.GIT_SERVICE).createRecord(git);
        } catch (Exception e) {
            log.error("Error while creating git =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Git git) {
        try {
            log.debug("Updating Git with information: {}", git);
            return this.getHandlerFactory().getHandler(Handler.GIT_SERVICE).updateRecord(git);
        } catch (Exception e) {
            log.error("Error while updating git =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
