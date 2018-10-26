package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Tag;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class TagsHandler extends AbstractHandler<Tag> {

    @Override
    public Response getAllRecords(Tag tag) throws HandlerNotFoundException {
        try {
            paginationRecords = dataServiceBuilderFactory.getService(Handler.TAG_SERVICE).getAllRecords(tag);
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Tag tag) throws HandlerNotFoundException {
        try {
//            validatorBuilderFactory.getValidator(Handler.TAG_SERVICE).dataValidations(tag);
            User user  = currentUser(tag.getUserToken());
            tag.setUserId(user.getId());
            dataServiceBuilderFactory.getService(Handler.TAG_SERVICE).createRecord(tag);
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating role");
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Tag tag = (Tag) dataServiceBuilderFactory.getService(Handler.TAG_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildFetchRecordResponse(tag);
        } catch (AbstractException e){
            log.error("Exception while getting role");
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Tag tag) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.TAG_SERVICE).updateRecord(tag);
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating role");
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.TAG_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting role");
            return responseBuilderFactory.getResponseBuilder(Handler.TAG_SERVICE).buildErrorResponse(e);
        }
    }
}
