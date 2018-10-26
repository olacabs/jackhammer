package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.RoleUser;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleUserMapper implements  ResultSetMapper<RoleUser> {

    public RoleUser map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new RoleUser(resultSet.getLong("roleId"), resultSet.getLong("userId"));
    }
}