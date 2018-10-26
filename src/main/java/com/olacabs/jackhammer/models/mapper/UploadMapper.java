package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Upload;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UploadMapper implements ResultSetMapper<Upload> {
    public Upload map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Upload upload = new Upload();
        upload.setId(resultSet.getLong("id"));
        upload.setName(resultSet.getString("name"));
        upload.setFindingId(resultSet.getLong("findingId"));
        upload.setUserId(resultSet.getLong("userId"));
        upload.setCreatedAt(resultSet.getTimestamp("createdAt"));
        upload.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return upload;
    }
}
