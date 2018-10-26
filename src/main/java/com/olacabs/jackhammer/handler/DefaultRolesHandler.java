package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.DefaultRole;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;


@Slf4j
public class DefaultRolesHandler extends AbstractHandler<DefaultRole> {

    public Response getAllRecords(DefaultRole defaultRole) throws HandlerNotFoundException {
        return null;
    }

    public Response createRecord(DefaultRole defaultRole) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.DEFAULT_ROLE_SERVICE).dataValidations(defaultRole);
            dataServiceBuilderFactory.getService(Handler.DEFAULT_ROLE_SERVICE).createRecord(defaultRole);
            return responseBuilderFactory.getResponseBuilder(Handler.DEFAULT_ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching default role detail");
            return responseBuilderFactory.getResponseBuilder(Handler.DEFAULT_ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            DefaultRole defaultRole = (DefaultRole) dataServiceBuilderFactory.getService(Handler.DEFAULT_ROLE_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.DEFAULT_ROLE_SERVICE).buildFetchRecordResponse(defaultRole);
        } catch (AbstractException e) {
            log.error("Exception while fetching default role detail");
            return responseBuilderFactory.getResponseBuilder(Handler.DEFAULT_ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    public Response updateRecord(DefaultRole defaultRole) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.DEFAULT_ROLE_SERVICE).dataValidations(defaultRole);
            dataServiceBuilderFactory.getService(Handler.DEFAULT_ROLE_SERVICE).updateRecord(defaultRole);
            return responseBuilderFactory.getResponseBuilder(Handler.DEFAULT_ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while fetching default role details");
            return responseBuilderFactory.getResponseBuilder(Handler.DEFAULT_ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
