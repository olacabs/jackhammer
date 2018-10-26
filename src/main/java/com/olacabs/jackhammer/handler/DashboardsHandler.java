package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Dashboard;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class DashboardsHandler extends AbstractHandler<Dashboard> {

    @Override
    public Response getAllRecords(Dashboard dashboard) throws HandlerNotFoundException {
        try {
            User user = currentUser(dashboard.getUserToken());
            dashboard.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.DASHBOARD_SERVICE).getAllRecords(dashboard);
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Dashboard dashboard) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.DASHBOARD_SERVICE).dataValidations(dashboard);
            validatorBuilderFactory.getValidator(Handler.DASHBOARD_SERVICE).uniquenessValidations(dashboard);
            dataServiceBuilderFactory.getService(Handler.DASHBOARD_SERVICE).createRecord(dashboard);
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating dashboard");
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Dashboard dashboard = (Dashboard) dataServiceBuilderFactory.getService(Handler.DASHBOARD_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildFetchRecordResponse(dashboard);
        } catch (AbstractException e){
            log.error("Exception while getting dashboard");
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Dashboard dashboard) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.DASHBOARD_SERVICE).updateRecord(dashboard);
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating dashboard");
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.DASHBOARD_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting dashboard");
            return responseBuilderFactory.getResponseBuilder(Handler.DASHBOARD_SERVICE).buildErrorResponse(e);
        }
    }
}
