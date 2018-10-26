package com.olacabs.jackhammer.validations;

import com.google.inject.Inject;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.exceptions.InvalidcredentialException;
import com.olacabs.jackhammer.models.AbstractModel;
import com.olacabs.jackhammer.service.factories.DataServiceBuilderFactory;

public abstract class AbstractValidator<T extends AbstractModel> {

    @Inject
    DataServiceBuilderFactory dataServiceBuilderFactory;

    public abstract void dataValidations(T model) throws ValidationFailedException;
    public abstract void uniquenessValidations(T model) throws ValidationFailedException;
    public void userAuthValidations(T loggingUser,T dbUser) throws InvalidcredentialException {
        throw new InvalidcredentialException(ExceptionMessages.INVALID_CREDENTIALS,null, CustomErrorCodes.INVALID_CREDENTIALS);
    }
}
