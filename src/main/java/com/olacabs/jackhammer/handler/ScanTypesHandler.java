package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.ScanType;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ScanTypesHandler extends AbstractHandler<ScanType> {

    @Override
    public Response getAllRecords(ScanType scanType) throws HandlerNotFoundException {
        try {
            User user = currentUser(scanType.getUserToken());
            scanType.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).getAllRecords(scanType);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception while fetching ScanTypes");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(ScanType scanType) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.SCAN_TYPE_SERVICE).dataValidations(scanType);
            validatorBuilderFactory.getValidator(Handler.SCAN_TYPE_SERVICE).uniquenessValidations(scanType);
            dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).createRecord(scanType);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while creating ScanType");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            ScanType scanType = (ScanType) dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildFetchRecordResponse(scanType);
        } catch (AbstractException e) {
            log.error("Exception while getting ScanType");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(ScanType scanType) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.SCAN_TYPE_SERVICE).dataValidations(scanType);
            validatorBuilderFactory.getValidator(Handler.SCAN_TYPE_SERVICE).uniquenessValidations(scanType);
            dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).updateRecord(scanType);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while updating ScanType");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while deleting ScanType");
            return responseBuilderFactory.getResponseBuilder(Handler.SCAN_TYPE_SERVICE).buildErrorResponse(e);
        }
    }
}
