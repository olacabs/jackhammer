package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.Comment;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class CommentResponseBuilder extends AbstractResponseBuilder<Comment> {

    @Override
    public Response buildFetchRecordResponse(Comment comment) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(comment).build();
    }
}
