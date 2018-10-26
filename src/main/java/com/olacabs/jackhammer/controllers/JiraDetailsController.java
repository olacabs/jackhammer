package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.JiraDetail;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/api/v1/app/jira")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JiraDetailsController extends BaseController {


    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.JIRA_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting git =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }


    @POST
    public Response create(@Valid JiraDetail jiraDetail, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            jiraDetail.setUserToken(userToken);
            log.debug("Creating Role with information: {}", jiraDetail);
            return this.getHandlerFactory().getHandler(Handler.JIRA_SERVICE).createRecord(jiraDetail);
        } catch (Exception e){
            log.error("Error while creating Jira details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid JiraDetail jiraDetail) {
        try {
            log.debug("Updating Jira details with information: {}", jiraDetail);
            return this.getHandlerFactory().getHandler(Handler.JIRA_SERVICE).updateRecord(jiraDetail);

        } catch (Exception e) {
            log.error("Error while updating Jira details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
