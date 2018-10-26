package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.HardcodeSecret;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/api/v1/app/hardcode_secret")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HardcodeSecretsController extends BaseController {


    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.HARD_CODE_SECRET_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting hardcode secret details =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }


    @POST
    public Response create(@Valid HardcodeSecret hardcodeSecret, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            hardcodeSecret.setUserToken(userToken);
            log.debug("Creating Role with information: {}", hardcodeSecret);
            return this.getHandlerFactory().getHandler(Handler.HARD_CODE_SECRET_SERVICE).createRecord(hardcodeSecret);
        } catch (Exception e){
            log.error("Error while creating hardcode secret details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid HardcodeSecret hardcodeSecret) {
        try {
            log.debug("Updating hardcode secret details with information: {}", hardcodeSecret);
            return this.getHandlerFactory().getHandler(Handler.HARD_CODE_SECRET_SERVICE).updateRecord(hardcodeSecret);

        } catch (Exception e) {
            log.error("Error while updating hardcode secret details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
