package com.olacabs.jackhammer.handler;

import com.google.inject.Inject;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Upload;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.utilities.FileOperations;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;
import java.time.Instant;


import javax.ws.rs.core.Response;

@Slf4j
public class UploadsHandler extends AbstractHandler<Upload> {

    @Inject
    FileOperations fileOperations;

    @Override
    public Response getAllRecords(Upload upload) throws HandlerNotFoundException {
        try {
            paginationRecords = dataServiceBuilderFactory.getService(Handler.UPLOAD_SERVICE).getAllRecords(upload);
            return responseBuilderFactory.getResponseBuilder(Handler.UPLOAD_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.UPLOAD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Upload upload) throws HandlerNotFoundException {
        try {
//            validatorBuilderFactory.getValidator(Handler.UPLOAD_SERVICE).dataValidations(upload);
            String baseName = FilenameUtils.getBaseName(upload.getName());
            String extName = FilenameUtils.getExtension(upload.getName());
            User dbUser = currentUser(upload.getUserToken());
            String fileNameWithEpoc = baseName + '_' + Instant.now().toEpochMilli() + '.' + extName;
            upload.setUserId(dbUser.getId());
            upload.setName(fileNameWithEpoc);
            dataServiceBuilderFactory.getService(Handler.UPLOAD_SERVICE).createRecord(upload);
            fileOperations.copyUploadFile(upload.getUploadedInputStream(), upload.getFindingId(), fileNameWithEpoc); // COPY FILE TO USER DIR
            return responseBuilderFactory.getResponseBuilder(Handler.UPLOAD_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating role");
            return responseBuilderFactory.getResponseBuilder(Handler.UPLOAD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Upload role = (Upload) dataServiceBuilderFactory.getService(Handler.UPLOAD_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.UPLOAD_SERVICE).buildFetchRecordResponse(role);
        } catch (AbstractException e){
            log.error("Exception while getting role");
            return responseBuilderFactory.getResponseBuilder(Handler.UPLOAD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Upload upload) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).updateRecord(upload);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating role");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting role");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }
}
