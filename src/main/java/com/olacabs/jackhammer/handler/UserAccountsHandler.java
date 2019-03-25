package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.JwtToken;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class UserAccountsHandler extends AbstractHandler<User> {
    @Override
    public Response getAllRecords(User model) throws HandlerNotFoundException {
        return null;
    }

    //signup
    @Override
    public Response createRecord(User user) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.USER_SERVICE).dataValidations(user);
            validatorBuilderFactory.getValidator(Handler.USER_SERVICE).uniquenessValidations(user);
            User newUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).createRecord(user);
            JwtToken jwtToken = createJwtToken(newUser);
            newUser.setUserToken(jwtToken.getUserToken());
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).loginResponse(newUser);
        } catch (AbstractException e) {
            log.error("Exception while registering user");
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        return null;
    }

    //logout
    @Override
    public Response updateRecord(User user) throws HandlerNotFoundException {
        try {
            User authUser = jwtSecurity.authenticateUserToken(user.getUserToken());
            User dbUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(authUser);
            JwtToken jwtToken = new JwtToken();
            jwtToken.setUserId(dbUser.getId());
            dataServiceBuilderFactory.getService(Handler.JWT_SERVICE).updateRecord(jwtToken);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).logoutResponse();
        } catch (AbstractException e) {
            log.error("Exception while getting  logout", e);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
