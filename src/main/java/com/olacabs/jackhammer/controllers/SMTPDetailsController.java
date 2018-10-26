package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.SMTPDetail;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/api/v1/app/smtp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SMTPDetailsController extends BaseController {


    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SMTP_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting git =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }


    @POST
    public Response create(@Valid SMTPDetail smtpDetail, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            smtpDetail.setUserToken(userToken);
            log.debug("Creating Role with information: {}", smtpDetail);
            return this.getHandlerFactory().getHandler(Handler.SMTP_SERVICE).createRecord(smtpDetail);
        } catch (Exception e){
            log.error("Error while creating smtp details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid SMTPDetail smtpDetail) {
        try {
            log.debug("Updating smtp details with information: {}", smtpDetail);
            return this.getHandlerFactory().getHandler(Handler.SMTP_SERVICE).updateRecord(smtpDetail);

        } catch (Exception e) {
            log.error("Error while updating smtp details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
