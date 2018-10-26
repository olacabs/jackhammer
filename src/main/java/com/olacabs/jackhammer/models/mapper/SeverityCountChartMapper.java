package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.SeverityCountChart;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeverityCountChartMapper implements ResultSetMapper<SeverityCountChart> {
    public SeverityCountChart map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        SeverityCountChart severityCountDashboard = new SeverityCountChart();
        severityCountDashboard.setCount(resultSet.getLong("count"));
        severityCountDashboard.setSeverity(resultSet.getString("severity"));
        return severityCountDashboard;
    }
}
