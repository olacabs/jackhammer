package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class GroupsHandler extends AbstractHandler<Group> {

    @Override
    public Response getAllRecords(Group group) throws HandlerNotFoundException {
        try {
            User user = currentUser(group.getUserToken());
            group.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.GROUP_SERVICE).getAllRecords(group);
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching groups");
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Group group) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.GROUP_SERVICE).dataValidations(group);
            validatorBuilderFactory.getValidator(Handler.GROUP_SERVICE).uniquenessValidations(group);
            dataServiceBuilderFactory.getService(Handler.GROUP_SERVICE).createRecord(group);
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating Group");
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Group group = (Group) dataServiceBuilderFactory.getService(Handler.GROUP_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildFetchRecordResponse(group);
        } catch (AbstractException e){
            log.error("Exception while creating group");
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Group Group) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.GROUP_SERVICE).updateRecord(Group);
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating Group");
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.GROUP_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting group");
            return responseBuilderFactory.getResponseBuilder(Handler.GROUP_SERVICE).buildErrorResponse(e);
        }
    }
}
