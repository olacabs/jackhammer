package com.olacabs.jackhammer.controllers;


import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.Scan;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

@Api
@Path("/api/v1/app/scans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ScansController extends BaseController {

    @POST
    @Path("/list")
    public Response getAll(@Valid Scan scan, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            scan.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.SCAN_SERVICE).getAllRecords(scan);
        } catch (Exception e) {
            log.error("Error while getting scans =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SCAN_SERVICE).getRecord(id);
        } catch (Exception e) {
            log.error("Error while getting scans =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    @Path("/create_scan")
    public Response createScan(@Valid Scan scan, @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            scan.setUserToken(userToken);
            return this.getHandlerFactory().getHandler(Handler.SCAN_SERVICE).createRecord(scan);
        } catch (Exception e) {
            log.error("Error while creating scan =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createMobileScan(@FormDataParam("name") String name,
                           @FormDataParam("target") String target,
                           @FormDataParam("branch") String branch,
                           @FormDataParam("groupIds") List<Long> groupIds,
                           @FormDataParam("repoIds") List<Long> repoIds,
                           @FormDataParam("scheduleTypeId") long scheduleTypeId,
                           @FormDataParam("apkFile") InputStream apkFile,
                           @FormDataParam("apkFile") FormDataContentDisposition contentDisposition,
                           @FormDataParam("taskId") long taskId,
                           @FormDataParam("scanTypeId") long scanTypeId,
                           @FormDataParam("ownerTypeId") long ownerTypeId,
                           @FormDataParam("status") String status,
                           @FormDataParam("filename") String fileName,
                           @HeaderParam(HttpKeys.AUTHORIZATION) String userToken) {
        try {
            Scan scan = new Scan();
            scan.setName(name);
            scan.setTarget(target);
            scan.setBranch(branch);
            scan.setGroupIds(groupIds);
            scan.setRepoIds(repoIds);
            scan.setScheduleTypeId(scheduleTypeId);
            scan.setTaskId(taskId);
            scan.setScanTypeId(scanTypeId);
            scan.setOwnerTypeId(ownerTypeId);
            scan.setApkFile(apkFile);
            scan.setStatus(status);
            scan.setUserToken(userToken);
            if (contentDisposition != null) scan.setApkTempFile(contentDisposition.getFileName());
            log.debug("Creating scan with information: {}", scan);
            return this.getHandlerFactory().getHandler(Handler.SCAN_SERVICE).createRecord(scan);
        } catch (Exception e) {
            log.error("Error while creating scan =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Scan scan) {
        try {
            log.debug("Updating scan with information: {}", scan);
            scan.setId(id);
            return this.getHandlerFactory().getHandler(Handler.SCAN_SERVICE).updateRecord(scan);

        } catch (Exception e) {
            log.error("Error while updating scan =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        try {
            return this.getHandlerFactory().getHandler(Handler.SCAN_SERVICE).deleteRecord(id);
        } catch (Exception e) {
            log.error("Error while deleting scan =>", e);
            return this.getExceptionHandler().handle(e);
        }
    }

}
