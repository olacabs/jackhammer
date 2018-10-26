package com.olacabs.jackhammer.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.InvalidcredentialException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.utilities.EmailOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class UserValidator extends AbstractValidator<User> {

    private String passwordPattern;

    @Inject
    @Named(Constants.BCRYPT_PASSWORD_ENCODER)
    PasswordEncoder passwordEncoder;

    @Inject
    EmailOperations emailOperations;

    public UserValidator() {
        passwordPattern = null;
    }

    @Override
    public void dataValidations(User user) throws ValidationFailedException {
        try {
            doNullValidations(user);
            emailOperations.isValidEmail(user.getEmail());
            doPasswordValidations(user, false, false, false, 6, 24);
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }
    }

    @Override
    public void uniquenessValidations(User user) throws ValidationFailedException {
        User validUser = null;
        try {
            validUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(user);
        } catch (AbstractException e) {
            log.error("Handler not found while validating user");
        }
        if (validUser != null)
            throw new ValidationFailedException(ExceptionMessages.EMAIL_ALREADY_EXISTS, null, CustomErrorCodes.EMAIL_ALREADY_EXISTS);
    }

    @Override
    public void userAuthValidations(User loggingUser, User dbUser) throws InvalidcredentialException {
        if (dbUser == null)
            throw new InvalidcredentialException(ExceptionMessages.EMAIL_NOT_EXISTS, null, CustomErrorCodes.EMAIL_NOT_EXISTS);
        Boolean validPassword = passwordEncoder.matches(loggingUser.getPassword(), dbUser.getPassword());
        if (!validPassword)
            throw new InvalidcredentialException(ExceptionMessages.INVALID_CREDENTIALS, null, CustomErrorCodes.INVALID_CREDENTIALS);
    }

    private void doNullValidations(User user) {
        Preconditions.checkNotNull(user.getName());
        Preconditions.checkNotNull(user.getEmail());
        Preconditions.checkNotNull(user.getPassword());
    }

    private void doPasswordValidations(User user,
                                       boolean forceSpecialChar,
                                       boolean forceCapitalLetter,
                                       boolean forceNumber,
                                       int minLength,
                                       int maxLength) {

        StringBuilder patternBuilder = new StringBuilder(Constants.PASSWORD_STRING_BUILDER);

        if (forceSpecialChar) patternBuilder.append(Constants.PASSWORD_FORCE_SPECIAL_CHARATERS_REGEX);
        if (forceCapitalLetter) patternBuilder.append(Constants.PASSWORD_FORCE_CAPITAL_LETTERS_REGEX);
        if (forceNumber) patternBuilder.append(Constants.PASSWORD_FORCE_NUMBERS_REGEX);

        patternBuilder.append(Constants.PASSWORD_MIN_LENGTH_START);
        patternBuilder.append(minLength);
        patternBuilder.append(Constants.PASSWORD_LENGTH_SEPARATOR);
        patternBuilder.append(maxLength);
        patternBuilder.append (Constants.PASSWORD_MIN_LENGTH_END);
        passwordPattern = patternBuilder.toString();
        Pattern p = Pattern.compile(passwordPattern);
        Matcher m = p.matcher(user.getPassword());
//        Preconditions.checkState(m.matches());
    }

}

