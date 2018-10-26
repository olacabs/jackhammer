package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;

import com.olacabs.jackhammer.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;


import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.Scan;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanValidator  extends AbstractValidator<Scan> {

    @Override
    public void dataValidations(Scan scan)  throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(scan.getTarget());
            Boolean isValidUrl =  validateUrl(scan.getTarget());
            if(isValidUrl) return;
            Boolean isValidIp = validateIp(scan.getTarget());
            if(isValidIp) return;
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA,null, CustomErrorCodes.INVALID_DATA);
        } catch (NullPointerException npe){
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }

    }
    @Override
    public void uniquenessValidations(Scan scan) throws ValidationFailedException {

    }

    private boolean validateUrl(String target)
    {
        String[] schemes = {Constants.HTTP_PROTOCOL_PATTERN,Constants.HTTPS_PROTOCOL_PATTERN};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(target);
    }


    public boolean validateIp(String ip) {
        return InetAddressValidator.getInstance().isValid(ip) || StringUtils.equals(ip,Constants.LOCAL_HOST);
    }

}
