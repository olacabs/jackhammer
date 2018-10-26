package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.GroupRole;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRoleMapper implements ResultSetMapper<GroupRole> {

    public GroupRole map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        GroupRole groupRole = new GroupRole();
        groupRole.setGroupId(resultSet.getLong("groupId"));
        groupRole.setRoleId(resultSet.getLong("roleId"));
        return groupRole;
    }
}
