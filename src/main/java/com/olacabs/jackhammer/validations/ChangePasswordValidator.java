package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.InvalidcredentialException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ChangePasswordValidator extends AbstractValidator<User> {

    @Inject
    @Named(Constants.BCRYPT_PASSWORD_ENCODER)
    PasswordEncoder passwordEncoder;

    @Override
    public void dataValidations(User user) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(user.getPassword());
            Preconditions.checkNotNull(user.getNewPassword());
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }
    }

    @Override
    public void uniquenessValidations(User model) throws ValidationFailedException {

    }

    public void userAuthValidations(User loggingUser, User dbUser) throws InvalidcredentialException {
        Boolean validPassword = passwordEncoder.matches(loggingUser.getPassword(), dbUser.getPassword());
        if (!validPassword)
            throw new InvalidcredentialException(ExceptionMessages.INVALID_CURRENT_PASSWORD, null, CustomErrorCodes.INVALID_CURRENT_PASSWORD);
    }
}
