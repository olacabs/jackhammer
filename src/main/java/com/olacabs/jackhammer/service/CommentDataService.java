package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.CommentDAO;
import com.olacabs.jackhammer.models.*;

import java.util.List;


public class CommentDataService extends AbstractDataService<Comment> {

    @Inject
    @Named(Constants.COMMENT_DAO)
    CommentDAO commentDAO;

    @Override
    public PagedResponse<Comment> getAllRecords(Comment comment) {
         List<Comment> commentList =  commentDAO.getAll(comment);
         for(Comment eachComment : commentList) {
             User user = userDAOJdbi.get(eachComment.getUserId());
             eachComment.setUserName(user.getName());
         }
        paginationRecords.setItems(commentList);
        return paginationRecords;
    }

    @Override
    public Comment fetchRecordByname(Comment comment){
        return null;
    }

    @Override
    public Comment fetchRecordById(long id){
        return commentDAO.get(id);
    }

    @Override
    public Comment createRecord(Comment comment) {
        long id = commentDAO.insert(comment);
        return commentDAO.get(id);
    }
    @Override
    public void updateRecord(Comment comment){
        commentDAO.update(comment);
    }
    @Override
    public void deleteRecord(long id){
        roleDAOJdbi.delete(id);
    }
}
