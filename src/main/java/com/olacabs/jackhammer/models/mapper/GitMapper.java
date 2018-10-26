package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Git;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GitMapper implements ResultSetMapper<Git> {
    public Git map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Git git = new Git();
        git.setId(resultSet.getLong("id"));
        git.setUserName(resultSet.getString("userName"));
        git.setGitEndPoint(resultSet.getString("gitEndPoint"));
        git.setApiAccessToken(resultSet.getString("apiAccessToken"));
        git.setGitType(resultSet.getString("gitType"));
        git.setOrganizationName(resultSet.getString("organizationName"));
        return git;
    }
}
