package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Filter;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class FiltersHandler extends AbstractHandler<Filter> {

    @Override
    public Response getAllRecords(Filter filter) throws HandlerNotFoundException {
        try {
            User user = currentUser(filter.getUserToken());
            filter.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.FILTER_SERVICE).getAllRecords(filter);
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching filter results");
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Filter filter) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.FILTER_SERVICE).dataValidations(filter);
            validatorBuilderFactory.getValidator(Handler.FILTER_SERVICE).uniquenessValidations(filter);
            dataServiceBuilderFactory.getService(Handler.FILTER_SERVICE).createRecord(filter);
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating filter");
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Filter filter = (Filter) dataServiceBuilderFactory.getService(Handler.FILTER_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildFetchRecordResponse(filter);
        } catch (AbstractException e){
            log.error("Exception while getting filter");
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Filter filter) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.FILTER_SERVICE).updateRecord(filter);
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating filter");
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.FILTER_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting filter");
            return responseBuilderFactory.getResponseBuilder(Handler.FILTER_SERVICE).buildErrorResponse(e);
        }
    }
}
