package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Analytics;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class AnalyticsHandler extends AbstractHandler<Analytics> {

    @Override
    public Response getAllRecords(Analytics analytics) throws HandlerNotFoundException {
        try {
            User user = currentUser(analytics.getUserToken());
            analytics.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.ANALYTICS_SERVICE).getAllRecords(analytics);
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Analytics analytics) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.ANALYTICS_SERVICE).dataValidations(analytics);
            validatorBuilderFactory.getValidator(Handler.ANALYTICS_SERVICE).uniquenessValidations(analytics);
            dataServiceBuilderFactory.getService(Handler.ANALYTICS_SERVICE).createRecord(analytics);
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating analytics");
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Analytics analytics = (Analytics) dataServiceBuilderFactory.getService(Handler.ANALYTICS_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildFetchRecordResponse(analytics);
        } catch (AbstractException e){
            log.error("Exception while getting analytics");
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Analytics analytics) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.ANALYTICS_SERVICE).updateRecord(analytics);
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating analytics");
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.ANALYTICS_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting analytics");
            return responseBuilderFactory.getResponseBuilder(Handler.ANALYTICS_SERVICE).buildErrorResponse(e);
        }
    }
}
