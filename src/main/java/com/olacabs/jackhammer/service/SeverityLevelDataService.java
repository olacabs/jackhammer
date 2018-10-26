package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.SeverityLevelDAO;
import com.olacabs.jackhammer.models.NotificationMails;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.SeverityLevel;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class SeverityLevelDataService extends AbstractDataService<SeverityLevel> {

    @Inject
    @Named(Constants.SEVERITY_LEVEL_DAO)
    SeverityLevelDAO severityLevelDAO;


    public PagedResponse<SeverityLevel> getAllRecords(SeverityLevel severityLevel) {
        paginationRecords.setItems(severityLevelDAO.getAll());
        setCRUDPermissions(paginationRecords, severityLevel,getCurrentTask(Constants.SEVERITY_LEVEL,severityLevel.getOwnerTypeId()));
        return paginationRecords;
    }

    public SeverityLevel createRecord(SeverityLevel severityLevel) {
        return null;
    }

    public SeverityLevel fetchRecordByname(SeverityLevel model) {
        return null;
    }

    public SeverityLevel fetchRecordById(long id) {
        SeverityLevel severityLevel = severityLevelDAO.get();
        return severityLevel;
    }

    public void updateRecord(SeverityLevel severityLevel) {
        if (severityLevel.getRequestFromNotificationMail()) {
            Map<String, NotificationMails> notificationMailsMap = severityLevel.getNotificationMailsConfiguration();
            for (Map.Entry<String, NotificationMails> notificationMailsEntry : notificationMailsMap.entrySet()) {
                SeverityLevel newSeverity = new SeverityLevel();
                newSeverity.setName(StringUtils.capitalize(notificationMailsEntry.getKey()));
                newSeverity.setThreshHoldCount(notificationMailsEntry.getValue().getCount());
                newSeverity.setMailIds(notificationMailsEntry.getValue().getMails());
                severityLevelDAO.updateSeverityMailConfig(newSeverity);
            }
        } else {
            Map<Long, Boolean> severitiesStatus = severityLevel.getSeveritiesStatus();
            for (Map.Entry<Long, Boolean> severity : severitiesStatus.entrySet()) {
                SeverityLevel newSeverity = new SeverityLevel();
                newSeverity.setId(severity.getKey());
                newSeverity.setEnabled(severity.getValue());
                severityLevelDAO.updateSeverityStatus(newSeverity);
            }
        }
    }

    public void deleteRecord(long id) {

    }
}
