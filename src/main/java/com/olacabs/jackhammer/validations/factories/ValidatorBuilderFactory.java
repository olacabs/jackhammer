package com.olacabs.jackhammer.validations.factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.validations.AbstractValidator;

public class ValidatorBuilderFactory {

    @Inject
    @Named(Constants.USER_VALIDATIONS)
    AbstractValidator userValidator;

    @Inject
    @Named(Constants.GROUP_VALIDATIONS)
    AbstractValidator groupValidator;

    @Inject
    @Named(Constants.ROLE_VALIDATIONS)
    AbstractValidator roleValidator;

    @Inject
    @Named(Constants.PERMISSION_VALIDATIONS)
    AbstractValidator permissionValidator;

    @Inject
    @Named(Constants.SCAN_VALIDATIONS)
    AbstractValidator scanValidator;

    @Inject
    @Named(Constants.REPO_VALIDATIONS)
    AbstractValidator repoValidator;

    @Inject
    @Named(Constants.SCHEDULE_TYPE_VALIDATIONS)
    AbstractValidator scheduleTypeValidator;

    @Inject
    @Named(Constants.SMTP_DETAILS_VALIDATOR)
    AbstractValidator smtpDetailValidator;


    @Inject
    @Named(Constants.JIRA_DETAILS_VALIDATOR)
    AbstractValidator jiraDetailValidator;

    @Inject
    @Named(Constants.GIT_VALIDATIONS)
    AbstractValidator gitValidator;

    @Inject
    @Named(Constants.DEFAULT_ROLE_VALIDATOR)
    AbstractValidator defaultRoleValidator;

    @Inject
    @Named(Constants.SEVERITY_LEVEL_VALIDATOR)
    AbstractValidator severityLevelValidator;

    @Inject
    @Named(Constants.HARDCODE_SECRET_VALIDATOR)
    AbstractValidator hardcodeSecretValidator;

    @Inject
    @Named(Constants.SCAN_TYPE_VALIDATOR)
    AbstractValidator scanTypeValidator;

    @Inject
    @Named(Constants.TOOL_VALIDATIONS)
    AbstractValidator toolValidator;

    @Inject
    @Named(Constants.CHANGE_PASSWORD_VALIDATIONS)
    AbstractValidator changePasswordValidator;

    @Inject
    @Named(Constants.RESET_PASSWORD_VALIDATIONS)
    AbstractValidator resetPasswordValidator;

    public AbstractValidator getValidator(Handler Ehandle) throws HandlerNotFoundException {

        switch (Ehandle) {
            case USER_SERVICE:
                return userValidator;
            case GROUP_SERVICE:
                return groupValidator;
            case ROLE_SERVICE:
                return roleValidator;
            case PERMISSION_SERVICE:
                return permissionValidator;
            case SCAN_SERVICE:
                return scanValidator;
            case REPO_SERVICE:
                return repoValidator;
            case SCHEDULE_TYPE_SERVICE:
                return scheduleTypeValidator;
            case SMTP_SERVICE:
                return smtpDetailValidator;
            case JIRA_SERVICE:
                return jiraDetailValidator;
            case GIT_SERVICE:
                return gitValidator;
            case DEFAULT_ROLE_SERVICE:
                return defaultRoleValidator;
            case SEVERITY_LEVEL_SERVICE:
                return severityLevelValidator;
            case HARD_CODE_SECRET_SERVICE:
                return hardcodeSecretValidator;
            case SCAN_TYPE_SERVICE:
                return scanTypeValidator;
            case TOOL_SERVICE:
                return toolValidator;
            case CHANGE_PASSWORD_SERVICE:
                return changePasswordValidator;
            case RESET_PASSWORD_SERVICE:
                return resetPasswordValidator;
            default:
                throw new HandlerNotFoundException(ExceptionMessages.HANDLER_NOT_FOUND, null, CustomErrorCodes.SERVICE_INTERNAL_EXCEPTION);
        }
    }

}
