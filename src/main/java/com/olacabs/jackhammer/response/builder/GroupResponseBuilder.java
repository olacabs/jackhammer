package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.PagedResponse;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

public class GroupResponseBuilder extends AbstractResponseBuilder<Group> {
    @Override
    public Response buildFetchRecordResponse(Group group) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(group).build();
    }
}
