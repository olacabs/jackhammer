package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.GroupUser;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupUserMapper implements ResultSetMapper<GroupUser> {
    public GroupUser map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new GroupUser(resultSet.getLong("groupId"),
                resultSet.getLong("userId"));
    }
}
