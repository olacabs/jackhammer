package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.VulnerableType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VulnerableTypeMapper implements ResultSetMapper<VulnerableType> {
    public VulnerableType map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        VulnerableType vulnerableType = new VulnerableType();
        vulnerableType.setName(resultSet.getString("name"));
        return vulnerableType;
    }
}
