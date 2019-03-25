package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.JiraDetail;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;


@Slf4j
public class JiraDetailsHandler extends AbstractHandler<JiraDetail> {

    public Response getAllRecords(JiraDetail jiraDetail) throws HandlerNotFoundException {
        return null;
    }

    public Response createRecord(JiraDetail jiraDetail) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.JIRA_SERVICE).dataValidations(jiraDetail);
            dataServiceBuilderFactory.getService(Handler.JIRA_SERVICE).createRecord(jiraDetail);
            return responseBuilderFactory.getResponseBuilder(Handler.JIRA_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching jira detail");
            return responseBuilderFactory.getResponseBuilder(Handler.JIRA_SERVICE).buildErrorResponse(e);
        }
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            JiraDetail jiraDetail = (JiraDetail) dataServiceBuilderFactory.getService(Handler.JIRA_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.JIRA_SERVICE).buildFetchRecordResponse(jiraDetail);
        } catch (AbstractException e) {
            log.error("Exception while fetching jira detail");
            return responseBuilderFactory.getResponseBuilder(Handler.JIRA_SERVICE).buildErrorResponse(e);
        }
    }

    public Response updateRecord(JiraDetail jiraDetail) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.JIRA_SERVICE).dataValidations(jiraDetail);
            dataServiceBuilderFactory.getService(Handler.JIRA_SERVICE).updateRecord(jiraDetail);
            return responseBuilderFactory.getResponseBuilder(Handler.JIRA_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching jira details");
            return responseBuilderFactory.getResponseBuilder(Handler.JIRA_SERVICE).buildErrorResponse(e);
        }
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
