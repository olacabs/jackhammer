package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Comment;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements ResultSetMapper<Comment> {

    public Comment map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getLong("id"));
        comment.setName(resultSet.getString("name"));
        comment.setFindingId(resultSet.getLong("findingId"));
        comment.setUserId(resultSet.getLong("userId"));
        comment.setCreatedAt(resultSet.getTimestamp("createdAt"));
        comment.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return comment;
    }

}
