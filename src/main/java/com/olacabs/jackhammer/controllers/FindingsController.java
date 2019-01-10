package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Finding;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/app/findings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class FindingsController extends BaseController {

    public static boolean repoPage;
    public static String toolName;
    public static String ownerType;
    public static String scanType;
    public static long repoId;
    public static String userToken;

    @POST
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM})
    public Response list(@Valid Finding finding,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            finding.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.FINDING_SERVICE).getAllRecords(finding);
        } catch (Exception e){
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id,@QueryParam("repoPage") boolean repoPage,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            this.repoPage = repoPage;
            this.userToken = userToken;
            return this.getHandlerFactory().getHandler(Handler.FINDING_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Finding finding) {
        try {
            log.debug("Creating Role with information: {}", finding);
            return this.getHandlerFactory().getHandler(Handler.FINDING_SERVICE).createRecord(finding);
        } catch (Exception e){
            log.error("Error while creating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Finding finding,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            finding.setId(id);
            finding.setUserToken(userToken);
            log.debug("Updating repo with information: {}", finding);
            return this.getHandlerFactory().getHandler(Handler.FINDING_SERVICE).updateRecord(finding);

        } catch (Exception e) {
            log.error("Error while updating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id,@QueryParam("toolName") String toolName,
                           @QueryParam("ownerType") String ownerType,@QueryParam("scanType") String scanType,
                           @QueryParam("repoId") long repoId) {
        try {
            if(toolName!=null) {
                this.toolName = toolName;
                this.ownerType = ownerType;
                this.scanType = scanType;
                this.repoId = repoId;
            }
            return this.getHandlerFactory().getHandler(Handler.FINDING_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
