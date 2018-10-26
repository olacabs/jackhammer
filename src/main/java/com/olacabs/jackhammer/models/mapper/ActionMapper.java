package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Action;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionMapper  implements ResultSetMapper<Action> {
    public Action map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Action action = new Action();
        action.setId(resultSet.getLong("id"));
        action.setName(resultSet.getString("name"));
        action.setIconClass(resultSet.getString("iconClass"));
        action.setIsScanTypeModule(resultSet.getBoolean("isScanTypeModule"));
        return action;
    }
}
