package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.DataServiceNotFoundException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.ScanType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScanTypeValidator extends AbstractValidator<ScanType> {
    @Override
    public void dataValidations(ScanType scanType) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(scanType.getName());
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }
    }

    @Override
    public void uniquenessValidations(ScanType scanType) throws ValidationFailedException {
        ScanType dbScanType = null;
        try {
            dbScanType = (ScanType) dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).fetchRecordByname(scanType);
        } catch(DataServiceNotFoundException dne) {
            log.error("Handler not found while validating scanType",dne);
        }
        if (dbScanType != null)
            throw new ValidationFailedException(ExceptionMessages.SCAN_TYPE_ALREADY_EXISTS, null, CustomErrorCodes.SCAN_TYPE_ALREADY_EXISTS);
    }
}
