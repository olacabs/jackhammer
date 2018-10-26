package com.olacabs.jackhammer.controllers;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.User;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/api/v1/app/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class UsersController extends BaseController {

    @Inject
    @Named(Constants.BCRYPT_PASSWORD_ENCODER)
    PasswordEncoder passwordEncoder;

    @POST
    @Path("/list")
    public Response getAll(@Valid User user,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            user.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.USER_SERVICE).getAllRecords(user);
        } catch (Exception e){
            log.error("Error while getting users =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.USER_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting users =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid User user) {
        try {
            log.debug("Updating user with information: {}", user);
            return this.getHandlerFactory().getHandler(Handler.USER_SERVICE).updateRecord(user);

        } catch (Exception e) {
            log.error("Error while updating user =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.USER_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting user =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }


    @POST
    @Path("/change_password")
    public Response changePassword(@Valid User user,@HeaderParam(HttpKeys.AUTHORIZATION) String accessToken) {
        try {
            user.setUserToken(accessToken);
            if(user.getNewPassword()!=null) user.setNewPassword(passwordEncoder.encode(user.getNewPassword()));
            log.debug("Updating user with information: {}", user);
            return this.getHandlerFactory().getHandler(Handler.CHANGE_PASSWORD_SERVICE).updateRecord(user);

        } catch (Exception e) {
            log.error("Error while updating user =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
