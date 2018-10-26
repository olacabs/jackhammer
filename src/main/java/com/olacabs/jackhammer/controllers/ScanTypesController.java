package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.ScanType;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/scan_types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ScanTypesController extends BaseController {

    @POST
    @Path("/list")
    public Response getScanTypes(ScanType scanType,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            scanType.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.SCAN_TYPE_SERVICE).getAllRecords(scanType);
        } catch (Exception e){
            log.error("Error while getting ScanTypes =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SCAN_TYPE_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting ScanTypes =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    public Response create(@Valid ScanType scanType) {
        try {
            log.debug("Creating ScanType with information: {}", scanType);
            return this.getHandlerFactory().getHandler(Handler.SCAN_TYPE_SERVICE).createRecord(scanType);
        } catch (Exception e){
            log.error("Error while creating ScanType =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@Valid ScanType scanType) {
        try {
            log.debug("Updating ScanType with information: {}", scanType);
            return this.getHandlerFactory().getHandler(Handler.SCAN_TYPE_SERVICE).updateRecord(scanType);

        } catch (Exception e) {
            log.error("Error while updating ScanType =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SCAN_TYPE_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting ScanType =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
