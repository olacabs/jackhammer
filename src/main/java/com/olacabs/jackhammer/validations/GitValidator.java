package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.InvalidHostException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.Git;
import org.apache.commons.validator.routines.UrlValidator;

public class GitValidator extends AbstractValidator<Git> {
    public void dataValidations(Git git) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(git.getGitType());
            Preconditions.checkNotNull(git.getUserName());
            Preconditions.checkNotNull(git.getGitEndPoint());
            Preconditions.checkNotNull(git.getApiAccessToken());
            Boolean validHost = validateUrl(git.getGitEndPoint());
            if(!validHost) throw new InvalidHostException(ExceptionMessages.INVALID_DATA, null, CustomErrorCodes.INVALID_DATA);
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        } catch (InvalidHostException ih) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_END_POINT, ih, CustomErrorCodes.INVALID_END_POINT);
        }
    }

    public void uniquenessValidations(Git model) throws ValidationFailedException {

    }

    private boolean validateUrl(String endpoint)
    {
        String[] schemes = {Constants.HTTP_PROTOCOL_PATTERN,Constants.HTTPS_PROTOCOL_PATTERN};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(endpoint);
    }
}
