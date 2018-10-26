package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.HardcodeSecret;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;


@Slf4j
public class HardcodeSecretHandler extends AbstractHandler<HardcodeSecret> {

    public Response getAllRecords(HardcodeSecret hardcodeSecret) throws HandlerNotFoundException {
        return null;
    }

    public Response createRecord(HardcodeSecret hardcodeSecret) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.HARD_CODE_SECRET_SERVICE).createRecord(hardcodeSecret);
            return responseBuilderFactory.getResponseBuilder(Handler.HARD_CODE_SECRET_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching hardcode secret detail");
            return responseBuilderFactory.getResponseBuilder(Handler.HARD_CODE_SECRET_SERVICE).buildErrorResponse(e);
        }
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            HardcodeSecret hardcodeSecret = (HardcodeSecret) dataServiceBuilderFactory.getService(Handler.HARD_CODE_SECRET_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.HARD_CODE_SECRET_SERVICE).buildFetchRecordResponse(hardcodeSecret);
        } catch (AbstractException e) {
            log.error("Exception while fetching hardcode secret detail");
            return responseBuilderFactory.getResponseBuilder(Handler.HARD_CODE_SECRET_SERVICE).buildErrorResponse(e);
        }
    }

    public Response updateRecord(HardcodeSecret hardcodeSecret) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.HARD_CODE_SECRET_SERVICE).updateRecord(hardcodeSecret);
            return responseBuilderFactory.getResponseBuilder(Handler.HARD_CODE_SECRET_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching hardcode secret details");
            return responseBuilderFactory.getResponseBuilder(Handler.HARD_CODE_SECRET_SERVICE).buildErrorResponse(e);
        }
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
