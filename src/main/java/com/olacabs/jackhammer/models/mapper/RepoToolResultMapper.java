package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.RepoToolResult;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepoToolResultMapper implements ResultSetMapper<RepoToolResult> {
    public RepoToolResult map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        RepoToolResult repoToolResult = new RepoToolResult();
        repoToolResult.setCount(resultSet.getLong("count"));
        repoToolResult.setToolName(resultSet.getString("toolName"));
        return repoToolResult;
    }
}
