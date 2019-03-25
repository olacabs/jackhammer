package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Role;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.GenericEntity;

import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
public class RoleResponseBuilder extends AbstractResponseBuilder<Role> {

    @Override
    public Response buildFetchRecordResponse(Role role) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(role).build();
    }
}
