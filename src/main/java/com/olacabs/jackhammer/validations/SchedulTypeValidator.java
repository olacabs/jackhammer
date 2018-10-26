package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.ScheduleType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchedulTypeValidator extends AbstractValidator<ScheduleType> {

    @Override
    public void dataValidations(ScheduleType scheduleType)  throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(scheduleType.getName());
        } catch (NullPointerException npe){
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }

    }
    @Override
    public void uniquenessValidations(ScheduleType scheduleType) throws ValidationFailedException {
        ScheduleType schedulePresent = null;
        try {
            schedulePresent =  (ScheduleType) dataServiceBuilderFactory.getService(Handler.SCHEDULE_TYPE_SERVICE).fetchRecordByname(scheduleType);
            if(schedulePresent!=null)
                throw new ValidationFailedException(ExceptionMessages.SCHEDULED_TYPE_ALREADY_EXISTS,null,CustomErrorCodes.SCHEDULED_TYPE_ALREADY_EXISTS);
        } catch (AbstractException e){
            log.error("Handler not found while validating SCHEDULED TYPE");
        }
        if(schedulePresent!=null) throw new ValidationFailedException(ExceptionMessages.SCHEDULED_TYPE_ALREADY_EXISTS,null,CustomErrorCodes.SCHEDULED_TYPE_ALREADY_EXISTS);

    }
}
