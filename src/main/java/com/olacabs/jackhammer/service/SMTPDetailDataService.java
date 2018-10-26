package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.SMTPDetailDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.SMTPDetail;
import com.olacabs.jackhammer.security.AES;

public class SMTPDetailDataService extends AbstractDataService<SMTPDetail> {

    @Inject
    @Named(Constants.SMTP_DETAIL_DAO)
    SMTPDetailDAO smtpDetailDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    public PagedResponse<SMTPDetail> getAllRecords(SMTPDetail smtpDetail) {
        return null;
    }

    public SMTPDetail createRecord(SMTPDetail smtpDetail) {
        SMTPDetail dbSMTPDetail = smtpDetailDAO.get();
        if (dbSMTPDetail == null) {
            smtpDetail.setSmtpPassword(AES.encrypt(smtpDetail.getSmtpPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
            smtpDetailDAO.insert(smtpDetail);
        }
        return dbSMTPDetail;
    }

    public SMTPDetail fetchRecordByname(SMTPDetail model) {
        return null;
    }

    public SMTPDetail fetchRecordById(long id) {
        SMTPDetail smtpDetail = smtpDetailDAO.get();
        if(smtpDetail!=null) smtpDetail.setSmtpPassword(AES.decrypt(smtpDetail.getSmtpPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
        return smtpDetail;
    }

    public void updateRecord(SMTPDetail smtpDetail) {
        smtpDetail.setSmtpPassword(AES.encrypt(smtpDetail.getSmtpPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
        smtpDetailDAO.update(smtpDetail);
    }

    public void deleteRecord(long id) {

    }
}
