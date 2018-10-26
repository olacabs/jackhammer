package com.olacabs.jackhammer.handler.factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.handler.AbstractHandler;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.common.Constants;

public class HandlerFactory {

    @Inject
    @Named(Constants.USER_ACCOUNTS_HANDLER)
    AbstractHandler userAccountsHandler;

    @Inject
    @Named(Constants.USERS_HANDLER)
    AbstractHandler usersHandler;

    @Inject
    @Named(Constants.GROUPS_HANDLER)
    AbstractHandler groupsHandler;

    @Inject
    @Named(Constants.ROLES_HANDLER)
    AbstractHandler rolesHandler;

    @Inject
    @Named(Constants.PERMISSIONS_HANDLER)
    AbstractHandler permissionHandler;

    @Inject
    @Named(Constants.SCANS_HANDLER)
    AbstractHandler scanHandler;

    @Inject
    @Named(Constants.REPOS_HANDLER)
    AbstractHandler repoHandler;

    @Inject
    @Named(Constants.SCHEDULE_TYPES_HANDLER)
    AbstractHandler scheduleTypeHandler;

    @Inject
    @Named(Constants.FINDING_HANDLER)
    AbstractHandler findingHandler;

    @Inject
    @Named(Constants.COMMENT_HANDLER)
    AbstractHandler commentHandler;

    @Inject
    @Named(Constants.TAG_HANDLER)
    AbstractHandler tagHandler;

    @Inject
    @Named(Constants.UPLOAD_HANDLER)
    AbstractHandler uploadHandler;

    @Inject
    @Named(Constants.TOOL_HANDLER)
    AbstractHandler toolHandler;

    @Inject
    @Named(Constants.LANGUAGE_HANDLER)
    AbstractHandler languageHandler;

    @Inject
    @Named(Constants.SCAN_TYPE_HANDLER)
    AbstractHandler scanTypeHandler;

    @Inject
    @Named(Constants.ACTION_HANDLER)
    AbstractHandler actionHandler;

    @Inject
    @Named(Constants.TASK_HANDLER)
    AbstractHandler taskHandler;


    @Inject
    @Named(Constants.GIT_HANDLER)
    AbstractHandler gitHandler;

    @Inject
    @Named(Constants.SMTP_DETAILS_HANDLER)
    AbstractHandler smtpDetailHandler;

    @Inject
    @Named(Constants.JIRA_DETAILS_HANDLER)
    AbstractHandler jiraDetailHandler;

    @Inject
    @Named(Constants.DEFAULT_ROLE_HANDLER)
    AbstractHandler defaultRoleHandlerHandler;

    @Inject
    @Named(Constants.SEVERITY_LEVEL_HANDLER)
    AbstractHandler severityLevelHandler;

    @Inject
    @Named(Constants.HARDCODE_SECRET_HANDLER)
    AbstractHandler hardcodeSecretHandler;

    @Inject
    @Named(Constants.DASHBOARD_HANDLER)
    AbstractHandler dashboardHandler;

    @Inject
    @Named(Constants.APPLICATION_HANDLER)
    AbstractHandler applicationHandler;

    @Inject
    @Named(Constants.ANALYTICS_HANDLER)
    AbstractHandler analyticsHandler;

    @Inject
    @Named(Constants.FILTERS_HANDLER)
    AbstractHandler filtersHandler;

    @Inject
    @Named(Constants.CHANGE_PASSWORD_HANDLER)
    AbstractHandler changePasswordHandler;

    @Inject
    @Named(Constants.RESET_PASSWORD_HANDLER)
    AbstractHandler resetPasswordHandler;


    public AbstractHandler getHandler(Handler handleEnum) throws HandlerNotFoundException {

        switch (handleEnum) {
            case USER_ACCOUNT_SERVICE:
                return userAccountsHandler;
            case USER_SERVICE:
                return usersHandler;
            case GROUP_SERVICE:
                return groupsHandler;
            case ROLE_SERVICE:
                return rolesHandler;
            case PERMISSION_SERVICE:
                return permissionHandler;
            case SCAN_SERVICE:
                return scanHandler;
            case REPO_SERVICE:
                return repoHandler;
            case SCHEDULE_TYPE_SERVICE:
                return scheduleTypeHandler;
            case FINDING_SERVICE:
                return findingHandler;
            case COMMENT_SERVICE:
                return commentHandler;
            case TAG_SERVICE:
                return tagHandler;
            case UPLOAD_SERVICE:
                return uploadHandler;
            case TOOL_SERVICE:
                return toolHandler;
            case LANGUAGE_SERVICE:
                return languageHandler;
            case SCAN_TYPE_SERVICE:
                return scanTypeHandler;
            case ACTION_SERVICE:
                return actionHandler;
            case TASK_SERVICE:
                return taskHandler;
            case GIT_SERVICE:
                return gitHandler;
            case SMTP_SERVICE:
                return smtpDetailHandler;
            case JIRA_SERVICE:
                return jiraDetailHandler;
            case DEFAULT_ROLE_SERVICE:
                return defaultRoleHandlerHandler;
            case SEVERITY_LEVEL_SERVICE:
                return severityLevelHandler;
            case HARD_CODE_SECRET_SERVICE:
                return hardcodeSecretHandler;
            case DASHBOARD_SERVICE:
                return dashboardHandler;
            case APPLICATION_SERVICE:
                return applicationHandler;
            case ANALYTICS_SERVICE:
                return analyticsHandler;
            case FILTER_SERVICE:
                return filtersHandler;
            case CHANGE_PASSWORD_SERVICE:
                return changePasswordHandler;
            case RESET_PASSWORD_SERVICE:
                return resetPasswordHandler;
            default:
                throw new HandlerNotFoundException(ExceptionMessages.HANDLER_NOT_FOUND, null, CustomErrorCodes.SERVICE_INTERNAL_EXCEPTION);
        }

    }
}
