package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.Permission;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
public class PermissionsHandler extends AbstractHandler<Permission> {

    @Override
    public Response getAllRecords(Permission permission) throws HandlerNotFoundException {
        try {
            paginationRecords = dataServiceBuilderFactory.getService(Handler.PERMISSION_SERVICE).getAllRecords(permission);
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception  while fetching permissions");
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Permission permission) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.PERMISSION_SERVICE).dataValidations(permission);
            validatorBuilderFactory.getValidator(Handler.PERMISSION_SERVICE).uniquenessValidations(permission);
            dataServiceBuilderFactory.getService(Handler.PERMISSION_SERVICE).createRecord(permission);
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating permission");
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Permission permission = (Permission) dataServiceBuilderFactory.getService(Handler.PERMISSION_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildFetchRecordResponse(permission);
        } catch (AbstractException e){
            log.error("Exception while fetching permission");
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Permission permission) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.PERMISSION_SERVICE).updateRecord(permission);
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating permission");
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.PERMISSION_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting permission");
            return responseBuilderFactory.getResponseBuilder(Handler.PERMISSION_SERVICE).buildErrorResponse(e);
        }
    }
}

