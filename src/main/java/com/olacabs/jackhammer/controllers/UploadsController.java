package com.olacabs.jackhammer.controllers;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Upload;
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
@Path("/api/v1/app/uploads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class UploadsController extends BaseController {
    @POST
    @Path("/list")
    public Response getRepos(Upload upload) {
        try {
            return this.getHandlerFactory().getHandler(Handler.UPLOAD_SERVICE).getAllRecords(upload);
        } catch (Exception e){
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.UPLOAD_SERVICE).getRecord(id);
        } catch(Exception e) {
            log.error("Error while getting repos =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@FormDataParam("file") InputStream uploadedInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileDetail,
                           @FormDataParam("findingId") long findingId,
                           @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            Upload upload = new Upload();
            upload.setUserToken(userToken);
            upload.setFindingId(findingId);
            upload.setUploadedInputStream(uploadedInputStream);
            upload.setFileDetail(fileDetail);
            upload.setName(fileDetail.getFileName());
            upload.setUserToken(userToken);
            log.debug("Creating upload with information: {}", upload);
            return this.getHandlerFactory().getHandler(Handler.UPLOAD_SERVICE).createRecord(upload);
        } catch (Exception e){
            log.error("Error while creating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Upload upload) {
        try {
            log.debug("Updating repo with information: {}", upload);
            return this.getHandlerFactory().getHandler(Handler.UPLOAD_SERVICE).updateRecord(upload);

        } catch (Exception e) {
            log.error("Error while updating repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.UPLOAD_SERVICE).deleteRecord(id);
        } catch(Exception e) {
            log.error("Error while deleting repo =>" , e);
            return this.getExceptionHandler().handle(e);
        }
    }
}
