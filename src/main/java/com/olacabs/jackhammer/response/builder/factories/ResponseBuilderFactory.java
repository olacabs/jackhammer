package com.olacabs.jackhammer.response.builder.factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.response.builder.AbstractResponseBuilder;
import com.olacabs.jackhammer.enums.Handler;


public class ResponseBuilderFactory {

    @Inject
    @Named(Constants.USER_RESPONSE_BUILDER)
    AbstractResponseBuilder userResponseBuilder;

    @Inject
    @Named(Constants.GROUP_RESPONSE_BUILDER)
    AbstractResponseBuilder groupResponseBuilder;

    @Inject
    @Named(Constants.ROLE_RESPONSE_BUILDER)
    AbstractResponseBuilder roleResponseBuilder;

    @Inject
    @Named(Constants.PERMISSION_RESPONSE_BUILDER)
    AbstractResponseBuilder permissionResponseBuilder;

    @Inject
    @Named(Constants.SCAN_RESPONSE_BUILDER)
    AbstractResponseBuilder scanResponseBuilder;

    @Inject
    @Named(Constants.REPO_RESPONSE_BUILDER)
    AbstractResponseBuilder repoResponseBuilder;

    @Inject
    @Named(Constants.SCHEDULE_TYPE_RESPONSE_BUILDER)
    AbstractResponseBuilder scheduleTypeResponseBuilder;

    @Inject
    @Named(Constants.FINDING_RESPONSE_BUILDER)
    AbstractResponseBuilder findingResponseBuilder;

    @Inject
    @Named(Constants.COMMENT_RESPONSE_BUILDER)
    AbstractResponseBuilder commentResponseBuilder;

    @Inject
    @Named(Constants.TAG_RESPONSE_BUILDER)
    AbstractResponseBuilder tagResponseBuilder;

    @Inject
    @Named(Constants.UPLOAD_RESPONSE_BUILDER)
    AbstractResponseBuilder uploadResponseBuilder;

    @Inject
    @Named(Constants.TOOL_RESPONSE_BUILDER)
    AbstractResponseBuilder toolResponseBuilder;

    @Inject
    @Named(Constants.LANGUAGE_RESPONSE_BUILDER)
    AbstractResponseBuilder languageResponseBuilder;

    @Inject
    @Named(Constants.SCAN_TYPE_RESPONSE_BUILDER)
    AbstractResponseBuilder scanTypeResponseBuilder;

    @Inject
    @Named(Constants.ACTION_RESPONSE_BUILDER)
    AbstractResponseBuilder actionResponseBuilder;

    @Inject
    @Named(Constants.TASK_RESPONSE_BUILDER)
    AbstractResponseBuilder taskResponseBuilder;

    @Inject
    @Named(Constants.GIT_RESPONSE_BUILDER)
    AbstractResponseBuilder gitResponseBuilder;

    @Inject
    @Named(Constants.SMTP_DETAIL_RESPONSE_BUILDER)
    AbstractResponseBuilder smtpResponseBuilder;

    @Inject
    @Named(Constants.JIRA_DETAIL_RESPONSE_BUILDER)
    AbstractResponseBuilder jiraResponseBuilder;


    @Inject
    @Named(Constants.DEFAULT_ROLE_RESPONSE_BUILDER)
    AbstractResponseBuilder defaultRoleResponseBuilder;

    @Inject
    @Named(Constants.SEVERITY_LEVEL_RESPONSE_BUILDER)
    AbstractResponseBuilder severityLevelResponseBuilder;

    @Inject
    @Named(Constants.HARDCODE_SECRET_RESPONSE_BUILDER)
    AbstractResponseBuilder hardcodeSecretResponseBuilder;

    @Inject
    @Named(Constants.DASHBOARD_RESPONSE_BUILDER)
    AbstractResponseBuilder dashboardResponseBuilder;


    @Inject
    @Named(Constants.APPLICATION_RESPONSE_BUILDER)
    AbstractResponseBuilder applicationResponseBuilder;


    @Inject
    @Named(Constants.ANALYTICS_RESPONSE_BUILDER)
    AbstractResponseBuilder analyticsResponseBuilder;

    @Inject
    @Named(Constants.CHANGE_PASSWORD_RESPONSE_BUILDER)
    AbstractResponseBuilder changePasswordResponseBuilder;

    @Inject
    @Named(Constants.RESET_PASSWORD_RESPONSE_BUILDER)
    AbstractResponseBuilder resetPasswordResponseBuilder;


    public  AbstractResponseBuilder getResponseBuilder(Handler Ehandle) throws HandlerNotFoundException {

        switch(Ehandle){
            case USER_SERVICE:
                return userResponseBuilder;
            case GROUP_SERVICE:
                return groupResponseBuilder;
            case ROLE_SERVICE:
                return roleResponseBuilder;
            case PERMISSION_SERVICE:
                return permissionResponseBuilder;
            case SCAN_SERVICE:
                return scanResponseBuilder;
            case REPO_SERVICE:
                return repoResponseBuilder;
            case SCHEDULE_TYPE_SERVICE:
                return scheduleTypeResponseBuilder;
            case FINDING_SERVICE:
                return findingResponseBuilder;
            case COMMENT_SERVICE:
                return commentResponseBuilder;
            case TAG_SERVICE:
                return tagResponseBuilder;
            case UPLOAD_SERVICE:
                return uploadResponseBuilder;
            case TOOL_SERVICE:
                return toolResponseBuilder;
            case LANGUAGE_SERVICE:
                return languageResponseBuilder;
            case SCAN_TYPE_SERVICE:
                return scanTypeResponseBuilder;
            case ACTION_SERVICE:
                return actionResponseBuilder;
            case TASK_SERVICE:
                return taskResponseBuilder;
            case GIT_SERVICE:
                return gitResponseBuilder;
            case SMTP_SERVICE:
                return smtpResponseBuilder;
            case JIRA_SERVICE:
                return jiraResponseBuilder;
            case DEFAULT_ROLE_SERVICE:
                return defaultRoleResponseBuilder;
            case SEVERITY_LEVEL_SERVICE:
                return severityLevelResponseBuilder;
            case HARD_CODE_SECRET_SERVICE:
                return hardcodeSecretResponseBuilder;
            case DASHBOARD_SERVICE:
                return dashboardResponseBuilder;
            case APPLICATION_SERVICE:
                return applicationResponseBuilder;
            case ANALYTICS_SERVICE:
                return analyticsResponseBuilder;
            case CHANGE_PASSWORD_SERVICE:
                return changePasswordResponseBuilder;
            case RESET_PASSWORD_SERVICE:
                return resetPasswordResponseBuilder;
            default:
                throw new HandlerNotFoundException(ExceptionMessages.HANDLER_NOT_FOUND,null, CustomErrorCodes.SERVICE_INTERNAL_EXCEPTION);
        }
    }

}
