package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Tag;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements ResultSetMapper<Tag> {
    public Tag map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong("id"));
        tag.setName(resultSet.getString("name"));
        return tag;
    }

}
