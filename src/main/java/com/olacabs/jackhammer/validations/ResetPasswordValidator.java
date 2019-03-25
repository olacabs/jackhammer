package com.olacabs.jackhammer.validations;

import com.google.inject.Inject;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.utilities.EmailOperations;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResetPasswordValidator extends AbstractValidator<User> {

    @Inject
    EmailOperations emailOperations;

    @Override
    public void dataValidations(User user) throws ValidationFailedException {
        try {
            emailOperations.isValidEmail(user.getEmail());
            User validUser = null;
            try {
                validUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(user);
            } catch (AbstractException e) {
                log.error("Handler not found while validating user");
            }
            if (validUser == null)
                throw new ValidationFailedException(ExceptionMessages.REST_PASSWORD_INSTRUCTIONS, null, CustomErrorCodes.REST_PASSWORD_INSTRUCTIONS);
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_MAIL, npe, CustomErrorCodes.INVALID_MAIL);
        }
    }

    @Override
    public void uniquenessValidations(User model) throws ValidationFailedException {

    }
}
