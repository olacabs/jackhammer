package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Permission;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionMapper  implements ResultSetMapper<Permission> {
    public Permission map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Permission permission = new Permission();
        permission.setId(resultSet.getLong("id"));
        permission.setName(resultSet.getString("name"));
        permission.setCreatedAt(resultSet.getTimestamp("createdAt"));
        permission.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return permission;
    }
}