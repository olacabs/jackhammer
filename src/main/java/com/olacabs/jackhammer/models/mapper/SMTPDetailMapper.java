package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.SMTPDetail;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SMTPDetailMapper implements ResultSetMapper<SMTPDetail> {

    public SMTPDetail map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        SMTPDetail smtpDetail = new SMTPDetail();
        smtpDetail.setApplicationUrl(resultSet.getString("applicationUrl"));
        smtpDetail.setSmtpHost(resultSet.getString("smtpHost"));
        smtpDetail.setSmtpUserName(resultSet.getString("smtpUserName"));
        smtpDetail.setSmtpPassword(resultSet.getString("smtpPassword"));
        smtpDetail.setSmtpPort(resultSet.getInt("smtpPort"));
        return smtpDetail;
    }

}
