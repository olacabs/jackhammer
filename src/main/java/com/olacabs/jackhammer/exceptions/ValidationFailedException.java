package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class ValidationFailedException extends AbstractException {

    public ValidationFailedException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }

}
