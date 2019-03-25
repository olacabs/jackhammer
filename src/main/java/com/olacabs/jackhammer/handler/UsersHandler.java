package com.olacabs.jackhammer.handler;

import javax.ws.rs.core.Response;

import com.olacabs.jackhammer.exceptions.*;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.models.JwtToken;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UsersHandler extends AbstractHandler<User> {

    @Override
    public Response getAllRecords(User user) throws HandlerNotFoundException {
        try {
            User dbUser = currentUser(user.getUserToken());
            user.setUser(dbUser);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.USER_SERVICE).getAllRecords(user);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch (AbstractException e) {
            log.error("Exception while fetching users");
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }

    //login
    @Override
    public Response createRecord(User user) throws HandlerNotFoundException {
        try {
            User dbUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(user);
            validatorBuilderFactory.getValidator(Handler.USER_SERVICE).userAuthValidations(user, dbUser);
            JwtToken jwtToken = createJwtToken(dbUser);
            dbUser.setUserToken(jwtToken.getUserToken());
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).loginResponse(dbUser);
        } catch (AbstractException e) {
            log.error("Exception  while registering user", e);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }


    //update user
    @Override
    public Response updateRecord(User user) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.USER_SERVICE).updateRecord(user);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while updating User");
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }


    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            User user = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildFetchRecordResponse(user);
        } catch (AbstractException e) {
            log.error("Exception while getting user");
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.USER_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while deleting user");
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).buildErrorResponse(e);
        }
    }
}
