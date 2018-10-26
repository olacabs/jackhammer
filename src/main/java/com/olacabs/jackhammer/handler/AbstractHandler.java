package com.olacabs.jackhammer.handler;

import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.DataServiceNotFoundException;
import com.olacabs.jackhammer.exceptions.UserNotFoundException;
import com.olacabs.jackhammer.models.JwtToken;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.security.JwtSecurity;
import com.olacabs.jackhammer.validations.factories.ValidatorBuilderFactory;
import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.response.builder.factories.ResponseBuilderFactory;
import com.olacabs.jackhammer.service.factories.DataServiceBuilderFactory;


@Slf4j
public abstract class AbstractHandler<T> {

    @Inject
    ValidatorBuilderFactory validatorBuilderFactory;

    @Inject
    DataServiceBuilderFactory dataServiceBuilderFactory;

    @Inject
    ResponseBuilderFactory responseBuilderFactory;

    @Inject
    JwtSecurity jwtSecurity;

    @Inject
    PagedResponse paginationRecords;

    public abstract Response getAllRecords(T model) throws HandlerNotFoundException;
    public abstract Response createRecord(T model) throws HandlerNotFoundException;
    public abstract Response getRecord(long id) throws HandlerNotFoundException;
    public abstract Response updateRecord(T model) throws HandlerNotFoundException;
    public abstract Response deleteRecord(long id) throws HandlerNotFoundException;

    protected User currentUser(String userToken) throws UserNotFoundException {
        try {
            User user = jwtSecurity.authenticateUserToken(userToken);
            User dbUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(user);
            return dbUser;
        } catch (AbstractException e) {
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND,e, CustomErrorCodes.USER_NOT_FOUND);
        }
    }

    protected JwtToken createJwtToken(User user) throws DataServiceNotFoundException {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setUserId(user.getId());
        return (JwtToken) dataServiceBuilderFactory.getService(Handler.JWT_SERVICE).createRecord(jwtToken);
    }
}
