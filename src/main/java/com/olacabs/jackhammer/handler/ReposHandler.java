package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.Permission;
import com.olacabs.jackhammer.models.Repo;

import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
public class ReposHandler extends AbstractHandler<Repo> {

    @Override
    public Response getAllRecords(Repo repo) throws HandlerNotFoundException {
        try {
            User user = currentUser(repo.getUserToken());
            repo.setUserId(user.getId());
            paginationRecords = dataServiceBuilderFactory.getService(Handler.REPO_SERVICE).getAllRecords(repo);
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception  while fetching repos");
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Repo repo) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.REPO_SERVICE).dataValidations(repo);
            validatorBuilderFactory.getValidator(Handler.REPO_SERVICE).uniquenessValidations(repo);
            dataServiceBuilderFactory.getService(Handler.REPO_SERVICE).createRecord(repo);
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating repo");
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Repo repo = (Repo) dataServiceBuilderFactory.getService(Handler.REPO_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildFetchRecordResponse(repo);
        } catch (AbstractException e){
            log.error("Exception while fetching repo");
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Repo repo) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.REPO_SERVICE).updateRecord(repo);
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating repo");
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.REPO_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting repo");
            return responseBuilderFactory.getResponseBuilder(Handler.REPO_SERVICE).buildErrorResponse(e);
        }
    }
}
