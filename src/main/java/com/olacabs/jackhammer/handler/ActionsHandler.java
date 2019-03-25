package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Action;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ActionsHandler extends AbstractHandler<Action> {
    public Response getAllRecords(Action action) throws HandlerNotFoundException {
        try {
            User user = currentUser(action.getUserToken());
            action.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.ACTION_SERVICE).getAllRecords(action);
            return responseBuilderFactory.getResponseBuilder(Handler.ACTION_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.ACTION_SERVICE).buildErrorResponse(e);
        }
    }

    public Response createRecord(Action model) throws HandlerNotFoundException {
        return null;
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        return null;
    }

    public Response updateRecord(Action model) throws HandlerNotFoundException {
        return null;
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
