package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.DefaultRole;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultRoleMapper  implements ResultSetMapper<DefaultRole> {

    public DefaultRole map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        DefaultRole defaultRole = new DefaultRole();
        defaultRole.setId(resultSet.getLong("id"));
        defaultRole.setRoleId(resultSet.getLong("roleId"));
        return defaultRole;
    }

}
