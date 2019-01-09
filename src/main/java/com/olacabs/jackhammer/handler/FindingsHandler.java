package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Finding;
import com.olacabs.jackhammer.models.Repo;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import javax.ws.rs.core.Response;

@Slf4j
public class FindingsHandler extends AbstractHandler<Finding> {

    @Override
    public Response getAllRecords(Finding finding) throws HandlerNotFoundException {
        try {
            User user = currentUser(finding.getUserToken());
            finding.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.FINDING_SERVICE).getAllRecords(finding);
            if (finding.getExportCSV() != null && finding.getExportCSV())
                return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).exportFindingsToCSV(paginationRecords);
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception  while fetching findings");
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Finding finding) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.FINDING_SERVICE).dataValidations(finding);
            validatorBuilderFactory.getValidator(Handler.FINDING_SERVICE).uniquenessValidations(finding);
            dataServiceBuilderFactory.getService(Handler.FINDING_SERVICE).createRecord(finding);
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while creating findings");
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Finding finding = (Finding) dataServiceBuilderFactory.getService(Handler.FINDING_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildFetchRecordResponse(finding);
        } catch (AbstractException e) {
            log.error("Exception while fetching finding");
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Finding finding) throws HandlerNotFoundException {
        try {
            User user = currentUser(finding.getUserToken());
            finding.setUser(user);
            dataServiceBuilderFactory.getService(Handler.FINDING_SERVICE).updateRecord(finding);
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while updating finding");
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.FINDING_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while deleting finding");
            return responseBuilderFactory.getResponseBuilder(Handler.FINDING_SERVICE).buildErrorResponse(e);
        }
    }
}
