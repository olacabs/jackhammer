package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Branch;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BranchMapper  implements ResultSetMapper<Branch> {
    public Branch map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Branch branch = new Branch();
        branch.setId(resultSet.getLong("id"));
        branch.setName(resultSet.getString("name"));
        branch.setRepoId(resultSet.getLong("repoId"));
        return branch;
    }
}

