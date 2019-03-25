package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.ScanType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScanTypeMapper implements ResultSetMapper<ScanType> {

    public ScanType map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ScanType scanType = new ScanType();
        scanType.setId(resultSet.getLong("id"));
        scanType.setName(resultSet.getString("name"));
        scanType.setIsStatic(resultSet.getBoolean("isStatic"));
        scanType.setIsWeb(resultSet.getBoolean("isWeb"));
        scanType.setIsMobile(resultSet.getBoolean("isMobile"));
        scanType.setIsWordpress(resultSet.getBoolean("isWordpress"));
        scanType.setIsNetwork(resultSet.getBoolean("isNetwork"));
        scanType.setIsHardCodeSecret(resultSet.getBoolean("isHardCodeSecret"));
        scanType.setCreatedAt(resultSet.getTimestamp("createdAt"));
        scanType.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return scanType;
    }
}
