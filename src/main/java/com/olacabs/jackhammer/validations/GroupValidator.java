package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.Group;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupValidator extends AbstractValidator<Group> {

    @Override
    public void dataValidations(Group group)  throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(group.getName());
        } catch (NullPointerException npe){
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }

    }
    @Override
    public void uniquenessValidations(Group group) throws ValidationFailedException {
        Group groupPresent = null;
        try {
            groupPresent =  (Group) dataServiceBuilderFactory.getService(Handler.GROUP_SERVICE).fetchRecordByname(group);
            if(groupPresent!=null)
                throw new ValidationFailedException(ExceptionMessages.GROUP_ALREADY_EXISTS,null,CustomErrorCodes.GROUP_ALREADY_EXISTS);
        } catch (AbstractException e){
            log.error("Handler not found while validating role");
        }
        if(groupPresent!=null) throw new ValidationFailedException(ExceptionMessages.GROUP_ALREADY_EXISTS,null,CustomErrorCodes.GROUP_ALREADY_EXISTS);

    }
}
