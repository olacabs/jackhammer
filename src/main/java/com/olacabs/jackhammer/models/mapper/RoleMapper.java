package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Role;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements ResultSetMapper<Role> {
    public Role map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        Role role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        role.setCreatedAt(resultSet.getTimestamp("createdAt"));
        role.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return role;
    }
}
