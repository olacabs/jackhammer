package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.SeverityLevel;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SeverityLevelMapper implements ResultSetMapper<SeverityLevel> {

    public SeverityLevel map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        SeverityLevel severityLevel = new SeverityLevel();
        severityLevel.setId(resultSet.getLong("id"));
        severityLevel.setName(resultSet.getString("name"));
        severityLevel.setEnabled(resultSet.getBoolean("enabled"));
        severityLevel.setThreshHoldCount(resultSet.getLong("threshHoldCount"));
        severityLevel.setMailIds(resultSet.getString("mailIds"));
        return severityLevel;
    }

}
