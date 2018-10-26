package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Scan;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScanMapper implements ResultSetMapper<Scan> {
    public Scan map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Scan scan = new Scan();
        scan.setId(resultSet.getLong("id"));
        scan.setName(resultSet.getString("name"));
        scan.setOwnerTypeId(resultSet.getLong("ownerTypeId"));
        scan.setScanTypeId(resultSet.getLong("scanTypeId"));
        scan.setStatus(resultSet.getString("status"));
        scan.setScheduleTypeId(resultSet.getLong("scheduleTypeId"));
        scan.setTarget(resultSet.getString("target"));
        scan.setRepoId(resultSet.getLong("repoId"));
        scan.setBranch(resultSet.getString("branch"));
        scan.setCreatedAt(resultSet.getTimestamp("createdAt"));
        scan.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        scan.setGroupId(resultSet.getLong("groupId"));
        scan.setUserId(resultSet.getLong("userId"));
        scan.setStatusReason(resultSet.getString("statusReason"));
        scan.setStartTime(resultSet.getTimestamp("startTime"));
        scan.setEndTime(resultSet.getTimestamp("endTime"));
        scan.setSupported(resultSet.getBoolean("supported"));
        scan.setCloneRequired(resultSet.getBoolean("cloneRequired"));
        scan.setIsTaggedTools(resultSet.getBoolean("isTaggedTools"));
        scan.setScanPlatforms(resultSet.getString("scanPlatforms"));
        scan.setCriticalCount(resultSet.getLong("criticalCount"));
        scan.setHighCount(resultSet.getLong("highCount"));
        scan.setMediumCount(resultSet.getLong("mediumCount"));
        scan.setLowCount(resultSet.getLong("lowCount"));
        scan.setInfoCount(resultSet.getLong("infoCount"));
        scan.setApkTempFile(resultSet.getString("apkTempFile"));
        scan.setLastRunDate(resultSet.getDate("lastRunDate"));
        return scan;
    }
}
