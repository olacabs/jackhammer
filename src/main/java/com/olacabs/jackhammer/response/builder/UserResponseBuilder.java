package com.olacabs.jackhammer.response.builder;


import javax.ws.rs.core.Response;

import com.olacabs.jackhammer.common.*;
import com.olacabs.jackhammer.models.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
public class UserResponseBuilder extends AbstractResponseBuilder<User> {

    @Override
    public Response buildFetchRecordResponse(User user) {
        User responseUser = new User();
        responseUser.setGroups(user.getGroups());
        responseUser.setRoles(user.getRoles());
        responseUser.setEmail(user.getEmail());
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(responseUser).build();
    }

}

