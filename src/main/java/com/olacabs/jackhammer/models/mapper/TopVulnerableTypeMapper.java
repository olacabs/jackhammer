package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.TopVulnerableType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TopVulnerableTypeMapper implements ResultSetMapper<TopVulnerableType> {
    public TopVulnerableType map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        TopVulnerableType topVulnerableType = new  TopVulnerableType();
        topVulnerableType.setCount(resultSet.getLong("count"));
        topVulnerableType.setVulnerabilityType(resultSet.getString( "vulnerabilityType"));
        topVulnerableType.setSeverity(resultSet.getString( "severity"));
        return topVulnerableType;
    }
}
