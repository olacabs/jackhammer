package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.JiraDetail;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JiraDetailMapper implements ResultSetMapper<JiraDetail> {

    public JiraDetail map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        JiraDetail jiraDetail = new JiraDetail();
        jiraDetail.setHost(resultSet.getString("host"));
        jiraDetail.setUserName(resultSet.getString("userName"));
        jiraDetail.setPassword(resultSet.getString("password"));
        jiraDetail.setDefaultProject(resultSet.getString("defaultProject"));
        return jiraDetail;
    }
}
