package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class InternalException extends AbstractException {

    public InternalException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
