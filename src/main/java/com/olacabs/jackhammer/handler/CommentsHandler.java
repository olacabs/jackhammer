package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Comment;
import com.olacabs.jackhammer.models.Role;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

@Slf4j
public class CommentsHandler extends AbstractHandler<Comment> {

    @Override
    public Response getAllRecords(Comment comment) throws HandlerNotFoundException {
        try {
            paginationRecords = dataServiceBuilderFactory.getService(Handler.COMMENT_SERVICE).getAllRecords(comment);
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching roles");
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Comment comment) throws HandlerNotFoundException {
        try {
            User user = currentUser(comment.getUserToken());
            comment.setUserId(user.getId());
            dataServiceBuilderFactory.getService(Handler.COMMENT_SERVICE).createRecord(comment);
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating role");
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Role role = (Role) dataServiceBuilderFactory.getService(Handler.COMMENT_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildFetchRecordResponse(role);
        } catch (AbstractException e){
            log.error("Exception while getting role");
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Comment comment) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).updateRecord(comment);
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating role");
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.COMMENT_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting role");
            return responseBuilderFactory.getResponseBuilder(Handler.COMMENT_SERVICE).buildErrorResponse(e);
        }
    }
}
