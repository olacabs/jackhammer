package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Language;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageMapper implements ResultSetMapper<Language> {

    public Language map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Language language = new Language();
        language.setId(resultSet.getLong("id"));
        language.setName(resultSet.getString("name"));
        language.setCreatedAt(resultSet.getTimestamp("createdAt"));
        language.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        language.setFileExtension(resultSet.getString("fileExtension"));
        return language;
    }
}
