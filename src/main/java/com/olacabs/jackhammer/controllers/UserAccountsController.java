package com.olacabs.jackhammer.controllers;


import javax.ws.rs.*;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.common.HttpKeys;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.common.Constants;

@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@Path("/api/v1")
public class UserAccountsController extends BaseController {

    @Inject
    @Named(Constants.BCRYPT_PASSWORD_ENCODER)
    PasswordEncoder passwordEncoder;

    @POST
    @Path("/signup")
    public Response signUp(@Valid User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.debug("Registering user account with information: {}", user);
            return this.getHandlerFactory().getHandler(Handler.USER_ACCOUNT_SERVICE).createRecord(user);
        } catch (Exception e){
            log.error("Error while registering user =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    @Path("/login")
    public Response login(@Valid User user){
        try {
            log.debug("Logging user with information: {}" , user);
            return this.getHandlerFactory().getHandler(Handler.USER_SERVICE).createRecord(user);
        } catch (Exception e){
            log.error("Error while logging.... =>" ,e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/app/logout")
    public Response logout(@HeaderParam(HttpKeys.AUTHORIZATION) String accessToken){
        try {
            User user = new User();
            user.setUserToken(accessToken);
            return this.getHandlerFactory().getHandler(Handler.USER_ACCOUNT_SERVICE).updateRecord(user);
        } catch (Exception e) {
            log.error("Error while getting logout =>" , e);
            return this.getExceptionHandler().handle(e);
        }

    }


}
