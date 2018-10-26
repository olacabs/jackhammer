package com.olacabs.jackhammer.models.mapper;



import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.StatementContext;

import com.olacabs.jackhammer.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper implements ResultSetMapper<User> {

    public User map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setCreatedAt(resultSet.getTimestamp("createdAt"));
        user.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return user;
    }
}
