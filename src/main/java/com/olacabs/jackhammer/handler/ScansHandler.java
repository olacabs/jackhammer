package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ScansHandler extends AbstractHandler<Scan> {
    @Override
    public Response getAllRecords(Scan scan) throws HandlerNotFoundException {
        try {
            User user = currentUser(scan.getUserToken());
            scan.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.SCAN_SERVICE).getAllRecords(scan);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception while fetching scans");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Scan scan) throws HandlerNotFoundException {
        try {
            if (scan.getRepoIds().size() == 0)
                validatorBuilderFactory.getValidator(Handler.SCAN_SERVICE).dataValidations(scan);
            User user = currentUser(scan.getUserToken());
            scan.setUser(user);
            dataServiceBuilderFactory.getService(Handler.SCAN_SERVICE).createRecord(scan);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while creating scan");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Scan scan = (Scan) dataServiceBuilderFactory.getService(Handler.SCAN_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildFetchRecordResponse(scan);
        } catch (AbstractException e) {
            log.error("Exception while getting scan");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Scan scan) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SCAN_SERVICE).updateRecord(scan);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while updating scan");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SCAN_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while deleting scan");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_SERVICE).buildErrorResponse(e);
        }
    }
}
