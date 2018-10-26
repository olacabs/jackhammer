package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.SeverityLevel;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.util.List;


@Slf4j
public class SeverityLevelsHandler extends AbstractHandler<SeverityLevel> {

    public Response getAllRecords(SeverityLevel severityLevel) throws HandlerNotFoundException {
        try {
            User user = currentUser(severityLevel.getUserToken());
            severityLevel.setUser(user);
            paginationRecords =  dataServiceBuilderFactory.getService(Handler.SEVERITY_LEVEL_SERVICE).getAllRecords(severityLevel);
            return responseBuilderFactory
                    .getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE)
                    .buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception while fetching jira detail");
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildErrorResponse(e);
        }
    }

    public Response createRecord(SeverityLevel severityLevel) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SEVERITY_LEVEL_SERVICE).createRecord(severityLevel);
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching jira detail");
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildErrorResponse(e);
        }
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            SeverityLevel severityLevel = (SeverityLevel) dataServiceBuilderFactory.getService(Handler.SEVERITY_LEVEL_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildFetchRecordResponse(severityLevel);
        } catch (AbstractException e) {
            log.error("Exception while fetching jira detail");
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildErrorResponse(e);
        }
    }

    public Response updateRecord(SeverityLevel severityLevel) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SEVERITY_LEVEL_SERVICE).updateRecord(severityLevel);
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching jira details");
            return responseBuilderFactory.getResponseBuilder(Handler.SEVERITY_LEVEL_SERVICE).buildErrorResponse(e);
        }
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
