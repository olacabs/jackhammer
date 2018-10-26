package com.olacabs.jackhammer.models.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.olacabs.jackhammer.models.ScanTool;

public class ScanToolMapper implements ResultSetMapper<ScanTool> {

    public ScanTool map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ScanTool scanTool = new ScanTool();
        scanTool.setToolId(resultSet.getLong("toolId"));
        scanTool.setScanId(resultSet.getLong("scanId"));
        scanTool.setStatus(resultSet.getString("status"));
        scanTool.setToolInstanceId(resultSet.getLong("toolInstanceId"));
        return scanTool;
    }
}
