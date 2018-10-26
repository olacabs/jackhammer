package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.JiraDetailDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.JiraDetail;
import com.olacabs.jackhammer.security.AES;

public class JiraDetailDataService extends AbstractDataService<JiraDetail> {

    @Inject
    @Named(Constants.JIRA_DETAIL_DAO)
    JiraDetailDAO jiraDetailDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    public PagedResponse<JiraDetail> getAllRecords(JiraDetail jiraDetail) {
        return null;
    }

    public JiraDetail createRecord(JiraDetail jiraDetail) {
        JiraDetail dbJiraDetail = jiraDetailDAO.get();
        if (dbJiraDetail == null) {
            jiraDetail.setPassword(AES.encrypt(jiraDetail.getPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
            jiraDetailDAO.insert(jiraDetail);
        }
        return dbJiraDetail;
    }

    public JiraDetail fetchRecordByname(JiraDetail model) {
        return null;
    }

    public JiraDetail fetchRecordById(long id) {
        JiraDetail jiraDetail = jiraDetailDAO.get();
        if(jiraDetail!=null) jiraDetail.setPassword(AES.decrypt(jiraDetail.getPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
        return jiraDetail;
    }

    public void updateRecord(JiraDetail jiraDetail) {
        jiraDetail.setPassword(AES.encrypt(jiraDetail.getPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
        jiraDetailDAO.update(jiraDetail);
    }

    public void deleteRecord(long id) {

    }
}
