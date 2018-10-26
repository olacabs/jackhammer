package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.InvalidHostException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.DefaultRole;
import com.olacabs.jackhammer.models.SMTPDetail;
import org.apache.commons.validator.routines.DomainValidator;

public class DefaultRoleValidator extends AbstractValidator<DefaultRole> {

    public void dataValidations(DefaultRole defaultRole) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(defaultRole.getRoleId());
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }
    }

    public void uniquenessValidations(DefaultRole model) throws ValidationFailedException {

    }
}
