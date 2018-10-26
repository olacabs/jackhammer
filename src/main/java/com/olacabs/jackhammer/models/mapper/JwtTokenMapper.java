package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.JwtToken;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JwtTokenMapper implements ResultSetMapper<JwtToken> {
    public JwtToken map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setId(resultSet.getLong("id"));
        jwtToken.setTokenValidFrom(resultSet.getDate("tokenValidFrom"));
        jwtToken.setTokenValidUntil(resultSet.getDate("tokenValidUntil"));
        jwtToken.setCreatedAt(resultSet.getTimestamp("createdAt"));
        jwtToken.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        jwtToken.setDeleted(resultSet.getBoolean("deleted"));
        jwtToken.setVersion(resultSet.getLong("version"));
        return jwtToken;
    }
}
