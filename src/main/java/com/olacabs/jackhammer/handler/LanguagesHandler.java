package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Language;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class LanguagesHandler extends AbstractHandler<Language> {

    @Override
    public Response getAllRecords(Language language) throws HandlerNotFoundException {
        try {
            paginationRecords = dataServiceBuilderFactory.getService(Handler.LANGUAGE_SERVICE).getAllRecords(language);
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Language language) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.LANGUAGE_SERVICE).dataValidations(language);
            validatorBuilderFactory.getValidator(Handler.LANGUAGE_SERVICE).uniquenessValidations(language);
            dataServiceBuilderFactory.getService(Handler.LANGUAGE_SERVICE).createRecord(language);
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating role");
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Language language = (Language) dataServiceBuilderFactory.getService(Handler.LANGUAGE_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildFetchRecordResponse(language);
        } catch (AbstractException e){
            log.error("Exception while getting role");
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Language language) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.LANGUAGE_SERVICE).updateRecord(language);
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating role");
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.LANGUAGE_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting role");
            return responseBuilderFactory.getResponseBuilder(Handler.LANGUAGE_SERVICE).buildErrorResponse(e);
        }
    }
}
