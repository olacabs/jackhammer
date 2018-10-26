package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.HttpResponseCodes;
import com.olacabs.jackhammer.models.Permission;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
public class PermissionResponseBuilder extends AbstractResponseBuilder<Permission> {

    @Override
    public Response buildFetchRecordResponse(Permission permission) {
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).entity(permission).build();
    }
}
