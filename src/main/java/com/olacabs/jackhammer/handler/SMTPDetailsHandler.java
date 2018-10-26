package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.SMTPDetail;
import lombok.extern.slf4j.Slf4j;


import javax.ws.rs.core.Response;


@Slf4j
public class SMTPDetailsHandler extends AbstractHandler<SMTPDetail> {

    public Response getAllRecords(SMTPDetail smtpDetail) throws HandlerNotFoundException {
        return null;
    }

    public Response createRecord(SMTPDetail smtpDetail) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.SMTP_SERVICE).dataValidations(smtpDetail);
            dataServiceBuilderFactory.getService(Handler.SMTP_SERVICE).createRecord(smtpDetail);
            return responseBuilderFactory.getResponseBuilder(Handler.SMTP_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching smtp details");
            return responseBuilderFactory.getResponseBuilder(Handler.SMTP_SERVICE).buildErrorResponse(e);
        }
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            SMTPDetail smtpDetail = (SMTPDetail) dataServiceBuilderFactory.getService(Handler.SMTP_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.SMTP_SERVICE).buildFetchRecordResponse(smtpDetail);
        } catch (AbstractException e) {
            log.error("Exception while fetching smtp details");
            return responseBuilderFactory.getResponseBuilder(Handler.SMTP_SERVICE).buildErrorResponse(e);
        }
    }

    public Response updateRecord(SMTPDetail smtpDetail) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.SMTP_SERVICE).dataValidations(smtpDetail);
            dataServiceBuilderFactory.getService(Handler.SMTP_SERVICE).updateRecord(smtpDetail);
            return responseBuilderFactory.getResponseBuilder(Handler.SMTP_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching smtp details");
            return responseBuilderFactory.getResponseBuilder(Handler.SMTP_SERVICE).buildErrorResponse(e);
        }
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
