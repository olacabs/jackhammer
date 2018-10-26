package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Group;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements ResultSetMapper<Group> {
    public Group map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        Group group = new Group();
        group.setId(resultSet.getLong("id"));
        group.setName(resultSet.getString("name"));
        group.setScanTypeId(resultSet.getLong("scanTypeId"));
        group.setCreatedAt(resultSet.getTimestamp("createdAt"));
        group.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return group;
    }
}
