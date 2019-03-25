package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.PermissionRole;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RolePermissionMapper implements ResultSetMapper<PermissionRole> {
    public PermissionRole map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        PermissionRole rolePermission = new PermissionRole();
        rolePermission.setRoleId(resultSet.getLong("roleId"));
        rolePermission.setPermissionId(resultSet.getLong("permissionId"));
        return rolePermission;
    }
}
