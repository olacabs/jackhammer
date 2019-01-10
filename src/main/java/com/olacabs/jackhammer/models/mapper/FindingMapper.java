package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.Finding;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindingMapper implements ResultSetMapper<Finding> {

    public Finding map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Finding finding = new Finding();
        finding.setId(resultSet.getLong("id"));
        finding.setName(resultSet.getString("name"));
        finding.setRepoId(resultSet.getLong("repoId"));
        finding.setScanId(resultSet.getLong("scanId"));
        finding.setSeverity(resultSet.getString("severity"));
        finding.setDescription(resultSet.getString("description"));
        finding.setToolName(resultSet.getString("toolName"));
        finding.setFileName(resultSet.getString("fileName"));
        finding.setCode(resultSet.getString("code"));
        finding.setExternalLink(resultSet.getString("externalLink"));
        finding.setCvssScore(resultSet.getString("cvssScore"));
        finding.setLocation(resultSet.getString("location"));
        finding.setUserInput(resultSet.getString("userInput"));
        finding.setAdvisory(resultSet.getString("advisory"));
        finding.setPort(resultSet.getString("port"));
        finding.setProtocol(resultSet.getString("protocol"));
        finding.setState(resultSet.getString("state"));
        finding.setProduct(resultSet.getString("product"));
        finding.setScripts(resultSet.getString("scripts"));
        finding.setVersion(resultSet.getString("version"));
        finding.setHost(resultSet.getString("host"));
        finding.setRequest(resultSet.getString("request"));
        finding.setResponse(resultSet.getString("response"));
        finding.setFingerprint(resultSet.getString("fingerprint"));
        finding.setStatus(resultSet.getString("status"));
        finding.setUserId(resultSet.getLong("userId"));
        finding.setIsFalsePositive(resultSet.getBoolean("isFalsePositive"));
        finding.setNotExploitable(resultSet.getBoolean("notExploitable"));
        finding.setCveCode(resultSet.getString("cveCode"));
        finding.setCweCode(resultSet.getString("cweCode"));
        finding.setPushedToJira(resultSet.getBoolean("pushedToJira"));
        finding.setModifiedBy(resultSet.getString("modifiedBy"));
//        finding.setSeverityCount(resultSet.getLong("severityCount"));
        return finding;
    }

}
