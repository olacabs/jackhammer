package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Task;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class TasksHandler extends AbstractHandler<Task> {
    public Response getAllRecords(Task task) throws HandlerNotFoundException {
        try {
            User user = currentUser(task.getUserToken());
            task.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.TASK_SERVICE).getAllRecords(task);
            return responseBuilderFactory.getResponseBuilder(Handler.TASK_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.TASK_SERVICE).buildErrorResponse(e);
        }
    }

    public Response createRecord(Task model) throws HandlerNotFoundException {
        return null;
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        return null;
    }

    public Response updateRecord(Task model) throws HandlerNotFoundException {
        return null;
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
