package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Group;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ApplicationsHandler extends AbstractHandler<Group> {

    @Override
    public Response getAllRecords(Group group) throws HandlerNotFoundException {
        try {
            paginationRecords = dataServiceBuilderFactory.getService(Handler.APPLICATION_SERVICE).getAllRecords(group);
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Group group) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.APPLICATION_SERVICE).dataValidations(group);
            validatorBuilderFactory.getValidator(Handler.APPLICATION_SERVICE).uniquenessValidations(group);
            dataServiceBuilderFactory.getService(Handler.APPLICATION_SERVICE).createRecord(group);
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating group");
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Group group = (Group) dataServiceBuilderFactory.getService(Handler.APPLICATION_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildFetchRecordResponse(group);
        } catch (AbstractException e){
            log.error("Exception while getting group");
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Group group) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.APPLICATION_SERVICE).updateRecord(group);
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating group");
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.APPLICATION_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting group");
            return responseBuilderFactory.getResponseBuilder(Handler.APPLICATION_SERVICE).buildErrorResponse(e);
        }
    }
}
