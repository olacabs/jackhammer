package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Application;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationMapper implements ResultSetMapper<Application> {
    public Application map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Application application = new Application();
        application.setId(resultSet.getLong("id"));
        application.setName(resultSet.getString("name"));
        application.setTotalCount(resultSet.getLong("totalCount"));
        application.setCriticalCount(resultSet.getLong("criticalCount"));
        application.setCriticalCount(resultSet.getLong("highCount"));
        application.setMediumCount(resultSet.getLong("mediumCount"));
        application.setLowCount(resultSet.getLong("lowCount"));
        application.setInfoCount(resultSet.getLong("infoCount"));
        return application;
    }
}
