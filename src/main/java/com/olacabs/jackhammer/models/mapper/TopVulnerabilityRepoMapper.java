package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.TopVulnerableRepo;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TopVulnerabilityRepoMapper implements ResultSetMapper<TopVulnerableRepo> {
    public TopVulnerableRepo map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        TopVulnerableRepo topVulnerabilityRepo = new TopVulnerableRepo();
        topVulnerabilityRepo.setCount(resultSet.getLong("count"));
        topVulnerabilityRepo.setRepoId(resultSet.getLong("repoId"));
        return topVulnerabilityRepo;
    }
}
