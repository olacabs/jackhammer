package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ChangePasswordHandler extends AbstractHandler<User> {
    @Override
    public Response getAllRecords(User model) throws HandlerNotFoundException {
        return null;
    }

    @Override
    public Response createRecord(User model) throws HandlerNotFoundException {
        return null;
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        return null;
    }

    @Override
    public Response updateRecord(User user) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.CHANGE_PASSWORD_SERVICE).dataValidations(user);
            User currentUser = currentUser(user.getUserToken());
            User dbUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(currentUser);
            validatorBuilderFactory.getValidator(Handler.CHANGE_PASSWORD_SERVICE).userAuthValidations(user, dbUser);
            user.setId(dbUser.getId());
            dataServiceBuilderFactory.getService(Handler.CHANGE_PASSWORD_SERVICE).updateRecord(user);
            return responseBuilderFactory.getResponseBuilder(Handler.CHANGE_PASSWORD_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while updating User");
            return responseBuilderFactory.getResponseBuilder(Handler.CHANGE_PASSWORD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
