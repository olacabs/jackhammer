package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeverityLevel extends AbstractModel {
    private Boolean enabled;
    private long threshHoldCount;
    private String mailIds;
    private Boolean requestFromNotificationMail;
    private Map<String,NotificationMails> notificationMailsConfiguration;
    private Map<Long,Boolean> severitiesStatus;
}
