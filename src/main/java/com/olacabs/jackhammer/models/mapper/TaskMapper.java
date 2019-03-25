package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Task;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper  implements ResultSetMapper<Task> {
    public Task map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Task task = new Task();
        task.setId(resultSet.getLong("id"));
        task.setName(resultSet.getString("name"));
        task.setTaskRoute(resultSet.getString("taskRoute"));
        task.setActionId(resultSet.getLong("actionId"));
        task.setParentId(resultSet.getLong("parentId"));
        task.setOwnerTypeId(resultSet.getLong("ownerTypeId"));
        task.setApiUrl(resultSet.getString("apiUrl"));
        task.setMethod(resultSet.getString("method"));
        task.setAccessToAll(resultSet.getBoolean("accessToAll"));
        return task;
    }
}

