package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Repo;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepoMapper implements ResultSetMapper<Repo> {
    public Repo map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Repo repo = new Repo();
        repo.setId(resultSet.getLong("id"));
        repo.setName(resultSet.getString("name"));
        repo.setTarget(resultSet.getString("target"));
        repo.setGroupId(resultSet.getLong("groupId"));
        repo.setUserId(resultSet.getLong("userId"));
        repo.setOwnerTypeId(resultSet.getLong("ownerTypeId"));
        repo.setScanTypeId(resultSet.getLong("scanTypeId"));
        return repo;
    }
}
