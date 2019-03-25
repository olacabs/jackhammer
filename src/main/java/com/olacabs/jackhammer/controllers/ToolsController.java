package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Tool;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Api
@Path("/api/v1/app/tools")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ToolsController extends BaseController {

    @POST
    @Path("/list")
    public Response getRepos(Tool tool,@HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            tool.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.TOOL_SERVICE).getAllRecords(tool);
        } catch (Exception e){
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TOOL_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(@FormDataParam("manifestFile") InputStream uploadedInputStream,
                           @FormDataParam("manifestFile") FormDataContentDisposition fileDetail,
                           @FormDataParam("name") String name,
                           @FormDataParam("scanTypeId") long scanTypeId,
                           @FormDataParam("languageId") long languageId,
                           @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            Tool tool = new Tool();
            tool.setName(name);
            tool.setUploadedInputStream(uploadedInputStream);
            tool.setScanTypeId(scanTypeId);
            tool.setLanguageId(languageId);
            log.debug("Creating tool with information: {}", tool);
            return this.getHandlerFactory().getHandler(Handler.TOOL_SERVICE).createRecord(tool);
        } catch (Exception e) {
            log.error("Error while creating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response update(@FormDataParam("manifestFile") InputStream uploadedInputStream,
                           @FormDataParam("manifestFile") FormDataContentDisposition fileDetail,
                           @FormDataParam("name") String name,
                           @FormDataParam("scanTypeId") long scanTypeId,
                           @FormDataParam("languageId") long languageId,
                           @FormDataParam("id") long id,
                           @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            Tool tool = new Tool();
            tool.setId(id);
            tool.setName(name);
            tool.setUploadedInputStream(uploadedInputStream);
            tool.setScanTypeId(scanTypeId);
            tool.setLanguageId(languageId);
            log.debug("Updating repo with information: {}", tool);
            return this.getHandlerFactory().getHandler(Handler.TOOL_SERVICE).updateRecord(tool);

        } catch (Exception e) {
            log.error("Error while updating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.TOOL_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
