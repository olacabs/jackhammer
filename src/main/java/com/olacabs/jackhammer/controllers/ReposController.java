package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Repo;
import com.olacabs.jackhammer.models.Role;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/repos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ReposController extends BaseController {

    @POST
    @Path("/list")
    public Response getRepos(Repo repo,@HeaderParam(HttpKeys.AUTHORIZATION) String accessToken) {
        try {
            repo.setUserToken(accessToken);
           return this.getHandlerFactory().getHandler(Handler.REPO_SERVICE).getAllRecords(repo);
        } catch (Exception e){
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.REPO_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Repo repo) {
        try {
            log.debug("Creating Role with information: {}", repo);
            return this.getHandlerFactory().getHandler(Handler.REPO_SERVICE).createRecord(repo);
        } catch (Exception e){
            log.error("Error while creating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Role role) {
        try {
            log.debug("Updating repo with information: {}", role);
            return this.getHandlerFactory().getHandler(Handler.REPO_SERVICE).updateRecord(role);

        } catch (Exception e) {
            log.error("Error while updating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.REPO_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
