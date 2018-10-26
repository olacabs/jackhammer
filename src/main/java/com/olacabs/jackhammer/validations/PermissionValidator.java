package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;

import com.olacabs.jackhammer.exceptions.AbstractException;
import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.Permission;

@Slf4j
public class PermissionValidator extends AbstractValidator<Permission> {

    @Override
    public void dataValidations(Permission permission)  throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(permission.getName());
        } catch (NullPointerException npe){
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }

    }
    @Override
    public void uniquenessValidations(Permission permission) throws ValidationFailedException {
        Permission permissionPresent = null;
        try {
             permissionPresent =  (Permission) dataServiceBuilderFactory.getService(Handler.PERMISSION_SERVICE).fetchRecordByname(permission);
            if(permissionPresent!=null)
                throw new ValidationFailedException(ExceptionMessages.PERMISSION_ALREADY_EXISTS,null,CustomErrorCodes.PERMISSION_ALREADY_EXISTS);
        } catch (AbstractException e){
            log.error("Handler not found while validating user");
        }
        if(permissionPresent!=null) throw new ValidationFailedException(ExceptionMessages.PERMISSION_ALREADY_EXISTS,null,CustomErrorCodes.PERMISSION_ALREADY_EXISTS);
    }
}
