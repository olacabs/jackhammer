package com.olacabs.jackhammer.service.factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.DataServiceNotFoundException;
import com.olacabs.jackhammer.service.AbstractDataService;

public class DataServiceBuilderFactory {

    @Inject
    @Named(Constants.USER_DATA_SERVICE)
    AbstractDataService userDataService;

    @Inject
    @Named(Constants.JWT_DATA_SERVICE)
    AbstractDataService jwtDataService;

    @Inject
    @Named(Constants.GROUP_DATA_SERVICE)
    AbstractDataService groupDataService;

    @Inject
    @Named(Constants.ROLE_DATA_SERVICE)
    AbstractDataService roleDataService;

    @Inject
    @Named(Constants.PERMISSION_DATA_SERVICE)
    AbstractDataService permissionDataService;

    @Inject
    @Named(Constants.SCAN_DATA_SERVICE)
    AbstractDataService scanDataService;

    @Inject
    @Named(Constants.REPO_DATA_SERVICE)
    AbstractDataService repoDataService;

    @Inject
    @Named(Constants.SCHEDULE_TYPE_DATA_SERVICE)
    AbstractDataService scheduleTypeDataService;

    @Inject
    @Named(Constants.LANGUAGE_DATA_SERVICE)
    AbstractDataService languageDataService;

    @Inject
    @Named(Constants.TOOL_DATA_SERVICE)
    AbstractDataService toolDataService;

    @Inject
    @Named(Constants.FINDING_DATA_SERVICE)
    AbstractDataService findingDataService;

    @Inject
    @Named(Constants.COMMENT_DATA_SERVICE)
    AbstractDataService commentDataService;

    @Inject
    @Named(Constants.TAG_DATA_SERVICE)
    AbstractDataService tagDataService;

    @Inject
    @Named(Constants.UPLOAD_DATA_SERVICE)
    AbstractDataService uploadDataService;

    @Inject
    @Named(Constants.SCAN_TYPE_DATA_SERVICE)
    AbstractDataService scanTypeDataService;

    @Inject
    @Named(Constants.ACTION_DATA_SERVICE)
    AbstractDataService actionDataService;

    @Inject
    @Named(Constants.TASK_DATA_SERVICE)
    AbstractDataService taskDataService;

    @Inject
    @Named(Constants.GIT_DATA_SERVICE)
    AbstractDataService gitDataService;

    @Inject
    @Named(Constants.SMTP_DETAIL_DATA_SERVICE)
    AbstractDataService smtpDataService;

    @Inject
    @Named(Constants.JIRA_DETAIL_DATA_SERVICE)
    AbstractDataService jiraDataService;

    @Inject
    @Named(Constants.DEFAULT_ROLE_DATA_SERVICE)
    AbstractDataService defaultRoleDataService;

    @Inject
    @Named(Constants.SEVERITY_LEVEL_DATA_SERVICE)
    AbstractDataService severityLevelDataService;

    @Inject
    @Named(Constants.HARDCODE_SECRET_DATA_SERVICE)
    AbstractDataService hardcodeSecretDataService;

    @Inject
    @Named(Constants.DASHBOARD_DATA_SERVICE)
    AbstractDataService dashboardDataService;

    @Inject
    @Named(Constants.APPLICATION_DATA_SERVICE)
    AbstractDataService applicationDataService;

    @Inject
    @Named(Constants.ANALYTICS_DATA_SERVICE)
    AbstractDataService analyticsDataService;

    @Inject
    @Named(Constants.FILTER_DATA_SERVICE)
    AbstractDataService filterDataService;

    @Inject
    @Named(Constants.CHANGE_PASSWORD_DATA_SERVICE)
    AbstractDataService changePasswordDataService;

    @Inject
    @Named(Constants.RESET_PASSWORD_DATA_SERVICE)
    AbstractDataService resetPasswordDataService;


    public AbstractDataService getService(Handler Ehandle) throws DataServiceNotFoundException {

        switch (Ehandle) {
            case USER_SERVICE:
                return userDataService;
            case JWT_SERVICE:
                return jwtDataService;
            case GROUP_SERVICE:
                return groupDataService;
            case ROLE_SERVICE:
                return roleDataService;
            case PERMISSION_SERVICE:
                return permissionDataService;
            case SCAN_SERVICE:
                return scanDataService;
            case REPO_SERVICE:
                return repoDataService;
            case SCHEDULE_TYPE_SERVICE:
                return scheduleTypeDataService;
            case LANGUAGE_SERVICE:
                return languageDataService;
            case TOOL_SERVICE:
                return toolDataService;
            case FINDING_SERVICE:
                return findingDataService;
            case COMMENT_SERVICE:
                return commentDataService;
            case TAG_SERVICE:
                return tagDataService;
            case UPLOAD_SERVICE:
                return uploadDataService;
            case SCAN_TYPE_SERVICE:
                return scanTypeDataService;
            case ACTION_SERVICE:
                return actionDataService;
            case TASK_SERVICE:
                return taskDataService;
            case GIT_SERVICE:
                return gitDataService;
            case SMTP_SERVICE:
                return smtpDataService;
            case JIRA_SERVICE:
                return jiraDataService;
            case DEFAULT_ROLE_SERVICE:
                return defaultRoleDataService;
            case SEVERITY_LEVEL_SERVICE:
                return severityLevelDataService;
            case HARD_CODE_SECRET_SERVICE:
                return hardcodeSecretDataService;
            case DASHBOARD_SERVICE:
                return dashboardDataService;
            case APPLICATION_SERVICE:
                return applicationDataService;
            case ANALYTICS_SERVICE:
                return analyticsDataService;
            case FILTER_SERVICE:
                return filterDataService;
            case CHANGE_PASSWORD_SERVICE:
                return changePasswordDataService;
            case RESET_PASSWORD_SERVICE:
                return resetPasswordDataService;
            default:
                throw new DataServiceNotFoundException(ExceptionMessages.HANDLER_NOT_FOUND, null, CustomErrorCodes.SERVICE_INTERNAL_EXCEPTION);
        }
    }
}
