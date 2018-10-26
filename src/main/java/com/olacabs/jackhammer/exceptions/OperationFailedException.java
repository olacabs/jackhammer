package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class OperationFailedException extends AbstractException {

    public OperationFailedException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
