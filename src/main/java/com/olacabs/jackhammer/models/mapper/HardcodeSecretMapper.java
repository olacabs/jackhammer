package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.HardcodeSecret;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HardcodeSecretMapper implements ResultSetMapper<HardcodeSecret> {

    public HardcodeSecret map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        HardcodeSecret hardcodeSecret = new HardcodeSecret();
        hardcodeSecret.setCommitsDepth(resultSet.getLong("commitsDepth"));
        hardcodeSecret.setCommitsStartDate(resultSet.getDate("commitsStartDate"));
        hardcodeSecret.setRegex(resultSet.getString("regex"));
        return hardcodeSecret;
    }

}
