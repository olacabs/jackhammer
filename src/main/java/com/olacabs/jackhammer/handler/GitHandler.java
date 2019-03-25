package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Git;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class GitHandler extends AbstractHandler<Git> {

    public Response getAllRecords(Git git) throws HandlerNotFoundException {
        try {
            User user = currentUser(git.getUserToken());
            git.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.GIT_SERVICE).getAllRecords(git);
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception while fetching git details");
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildErrorResponse(e);
        }
    }

    public Response createRecord(Git git) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.GIT_SERVICE).dataValidations(git);
            dataServiceBuilderFactory.getService(Handler.GIT_SERVICE).createRecord(git);
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching git details");
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildErrorResponse(e);
        }
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Git git = (Git) dataServiceBuilderFactory.getService(Handler.GIT_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildFetchRecordResponse(git);
        } catch (AbstractException e) {
            log.error("Exception while fetching git details");
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildErrorResponse(e);
        }
    }

    public Response updateRecord(Git git) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.GIT_SERVICE).dataValidations(git);
            dataServiceBuilderFactory.getService(Handler.GIT_SERVICE).updateRecord(git);
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching git details");
            return responseBuilderFactory.getResponseBuilder(Handler.GIT_SERVICE).buildErrorResponse(e);
        }
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
