package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Role;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class RolesHandler extends AbstractHandler<Role> {

    @Override
    public Response getAllRecords(Role role) throws HandlerNotFoundException {
        try {
            User user = currentUser(role.getUserToken());
            role.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).getAllRecords(role);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Role role) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.ROLE_SERVICE).dataValidations(role);
            validatorBuilderFactory.getValidator(Handler.ROLE_SERVICE).uniquenessValidations(role);
            dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).createRecord(role);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating role");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Role role = (Role) dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildFetchRecordResponse(role);
        } catch (AbstractException e){
            log.error("Exception while getting role");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Role role) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.ROLE_SERVICE).dataValidations(role);
            dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).updateRecord(role);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while updating role");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting role");
            return responseBuilderFactory.getResponseBuilder(Handler.ROLE_SERVICE).buildErrorResponse(e);
        }
    }
}
