package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.HttpResponseCodes;
import com.olacabs.jackhammer.models.Repo;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

public class RepoResponseBuilder extends  AbstractResponseBuilder<Repo> {

    @Override
    public Response buildFetchRecordResponse(Repo repo) {
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).entity(repo).build();
    }
}
