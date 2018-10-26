package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Dashboard;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/app/dashboard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class DashboardsController extends BaseController {

    @POST
    @Path("/view")
    public Response getAll(@Valid Dashboard dashboard,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            dashboard.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.DASHBOARD_SERVICE).getAllRecords(dashboard);
        } catch (Exception e){
            log.error("Error while getting dashboard info =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.DASHBOARD_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting dashboard info =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid Dashboard dashboard) {
        try {
            log.debug("Creating Dashboard with information: {}", dashboard);
            return this.getHandlerFactory().getHandler(Handler.DASHBOARD_SERVICE).createRecord(dashboard);
        } catch (Exception e){
            log.error("Error while creating dashboard =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Dashboard dashboard) {
        try {
            log.debug("Updating dashboard with information: {}", dashboard);
            return this.getHandlerFactory().getHandler(Handler.DASHBOARD_SERVICE).updateRecord(dashboard);

        } catch (Exception e) {
            log.error("Error while updating dashboard =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.DASHBOARD_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting dashboard =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
