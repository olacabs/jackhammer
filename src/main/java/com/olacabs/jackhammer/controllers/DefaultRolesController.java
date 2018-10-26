package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.DefaultRole;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/api/v1/app/default_role")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DefaultRolesController extends BaseController {


    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.DEFAULT_ROLE_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting git =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }


    @POST
    public Response create(@Valid DefaultRole defaultRole, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            defaultRole.setUserToken(userToken);
            log.debug("Creating Role with information: {}", defaultRole);
            return this.getHandlerFactory().getHandler(Handler.DEFAULT_ROLE_SERVICE).createRecord(defaultRole);
        } catch (Exception e){
            log.error("Error while creating smtp details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid DefaultRole defaultRole) {
        try {
            log.debug("Updating smtp details with information: {}", defaultRole);
            return this.getHandlerFactory().getHandler(Handler.DEFAULT_ROLE_SERVICE).updateRecord(defaultRole);

        } catch (Exception e) {
            log.error("Error while updating smtp details =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
