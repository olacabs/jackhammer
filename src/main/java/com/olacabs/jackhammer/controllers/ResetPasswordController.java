package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.common.HttpResponseCodes;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/v1")
public class ResetPasswordController  extends BaseController {

    @POST
    @Path("/send_reset_password_instructions")
    public Response sendResetPasswordInstructions(@Valid User user){
        try {
            return this.getHandlerFactory().getHandler(Handler.RESET_PASSWORD_SERVICE).createRecord(user);
        } catch (Exception e) {
            log.error("Error while getting logout =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/app/verify_reset_password_token")
    public Response verifyResetPasswordToken(){
       return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).build();
    }


    @POST
    @Path("/app/reset_password")
    public Response resetPassword(@Valid User user, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken){
        try {
            user.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.RESET_PASSWORD_SERVICE).updateRecord(user);
        } catch (Exception e) {
            log.error("Error while getting logout =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
