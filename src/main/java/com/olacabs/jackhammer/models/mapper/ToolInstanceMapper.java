package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.ToolInstance;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ToolInstanceMapper implements ResultSetMapper<ToolInstance> {
    public ToolInstance map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ToolInstance toolInstance = new ToolInstance();
        toolInstance.setId(resultSet.getLong("id"));
        toolInstance.setToolId(resultSet.getLong("toolId"));
        toolInstance.setMaxAllowedScans(resultSet.getLong("maxAllowedScans"));
        toolInstance.setCurrentRunningScans(resultSet.getLong("currentRunningScans"));
        toolInstance.setStatus(resultSet.getString("status"));
        toolInstance.setSessionId(resultSet.getString("sessionId"));
        toolInstance.setPlatform(resultSet.getString("platform"));
        toolInstance.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        toolInstance.setCreatedAt(resultSet.getTimestamp("createdAt"));
        return toolInstance;
    }
}
