package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.ScheduleType;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ScheduleTypesHandler extends AbstractHandler<ScheduleType>  {

    @Override
    public Response getAllRecords(ScheduleType scheduleType) throws HandlerNotFoundException {
        try {
            User user = currentUser(scheduleType.getUserToken());
            scheduleType.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.SCHEDULE_TYPE_SERVICE).getAllRecords(scheduleType);
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception  while fetching scheduleTypes");
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(ScheduleType scheduleType) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.SCHEDULE_TYPE_SERVICE).dataValidations(scheduleType);
            validatorBuilderFactory.getValidator(Handler.SCHEDULE_TYPE_SERVICE).uniquenessValidations(scheduleType);
            dataServiceBuilderFactory.getService(Handler.SCHEDULE_TYPE_SERVICE).createRecord(scheduleType);
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating scheduleType");
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            ScheduleType scheduleType = (ScheduleType) dataServiceBuilderFactory.getService(Handler.SCHEDULE_TYPE_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildFetchRecordResponse(scheduleType);
        } catch (AbstractException e){
            log.error("Exception while fetching scheduleType");
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(ScheduleType scheduleType) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SCHEDULE_TYPE_SERVICE).updateRecord(scheduleType);
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating scheduleTypes");
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SCHEDULE_TYPE_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting scheduleTypes");
            return responseBuilderFactory.getResponseBuilder(Handler.SCHEDULE_TYPE_SERVICE).buildErrorResponse(e);
        }
    }
}
