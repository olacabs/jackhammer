package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.models.User;

import javax.ws.rs.core.Response;

public class ResetPasswordResponseBuilder extends AbstractResponseBuilder<User> {
    @Override
    public Response buildFetchRecordResponse(User model) throws OperationFailedException {
        return null;
    }
}
