package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.OwnerType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerTypeMapper  implements ResultSetMapper<OwnerType> {
    public OwnerType map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        OwnerType ownerType = new OwnerType();
        ownerType.setId(resultSet.getLong("id"));
        ownerType.setName(resultSet.getString("name"));
        ownerType.setIsDefault(resultSet.getBoolean("isDefault"));
        return ownerType;
    }
}