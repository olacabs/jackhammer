package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.RoleTask;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RoleTaskMapper implements ResultSetMapper<RoleTask> {
    public RoleTask map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        RoleTask roleTask = new RoleTask();
        roleTask.setRoleId(resultSet.getLong("roleId"));
        roleTask.setTaskId(resultSet.getLong("taskId"));
        return roleTask;
    }
}