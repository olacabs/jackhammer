package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.ScheduleType;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleTypeMapper implements ResultSetMapper<ScheduleType> {
    public ScheduleType map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ScheduleType scheduleType = new ScheduleType();
        scheduleType.setId(resultSet.getLong("id"));
        scheduleType.setName(resultSet.getString("name"));
        scheduleType.setDays(resultSet.getInt("days"));
        return scheduleType;
    }
}
