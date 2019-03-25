package com.olacabs.jackhammer.validations;


import com.google.common.base.Preconditions;


import com.olacabs.jackhammer.exceptions.DataServiceNotFoundException;
import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.Role;

@Slf4j
public class RoleValidator extends AbstractValidator<Role> {

    @Override
    public void dataValidations(Role role) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(role.getName());
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }
    }

    @Override
    public void uniquenessValidations(Role role) throws ValidationFailedException {
        Role rolePresent = null;
        try {
            rolePresent = (Role) dataServiceBuilderFactory.getService(Handler.ROLE_SERVICE).fetchRecordByname(role);
        } catch(DataServiceNotFoundException dne) {
            log.error("Handler not found while validating role",dne);
        }
        if (rolePresent != null)
            throw new ValidationFailedException(ExceptionMessages.ROLE_ALREADY_EXISTS, null, CustomErrorCodes.ROLE_ALREADY_EXISTS);
    }
}
