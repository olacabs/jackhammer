package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.InvalidHostException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.JiraDetail;
import org.apache.commons.validator.routines.DomainValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JiraDetailValidator extends AbstractValidator<JiraDetail> {

    @Override
    public void dataValidations(JiraDetail jiraDetail) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(jiraDetail.getHost());
            Preconditions.checkNotNull(jiraDetail.getUserName());
            Preconditions.checkNotNull(jiraDetail.getPassword());
            Preconditions.checkNotNull(jiraDetail.getDefaultProject());
            Boolean validHost = DomainValidator.getInstance().isValid(jiraDetail.getHost());
            if(!validHost) throw new InvalidHostException(ExceptionMessages.INVALID_DATA, null, CustomErrorCodes.INVALID_DATA);
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        } catch (InvalidHostException ih) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_HOST, ih, CustomErrorCodes.INVALID_HOST);
        }
    }

    @Override
    public void uniquenessValidations(JiraDetail jiraDetail) throws ValidationFailedException {
    }
}
