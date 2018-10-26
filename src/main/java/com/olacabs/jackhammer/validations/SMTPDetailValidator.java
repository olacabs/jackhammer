package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.InvalidSmtpHost;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.SMTPDetail;
import org.apache.commons.validator.routines.DomainValidator;

public class SMTPDetailValidator extends AbstractValidator<SMTPDetail> {

    public void dataValidations(SMTPDetail smtpDetail) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(smtpDetail.getApplicationUrl());
            Preconditions.checkNotNull(smtpDetail.getSmtpHost());
            Preconditions.checkNotNull(smtpDetail.getSmtpPort());
            Preconditions.checkNotNull(smtpDetail.getSmtpPassword());
            Boolean validDomain = DomainValidator.getInstance().isValid(smtpDetail.getSmtpHost());
            if (!validDomain)
                throw new InvalidSmtpHost(ExceptionMessages.INVALID_DATA, null, CustomErrorCodes.INVALID_DATA);
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }
        catch (InvalidSmtpHost is) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_SMTP_HOST, is, CustomErrorCodes.INVALID_SMTP_HOST);
        }
    }

    public void uniquenessValidations(SMTPDetail model) throws ValidationFailedException {

    }
}
