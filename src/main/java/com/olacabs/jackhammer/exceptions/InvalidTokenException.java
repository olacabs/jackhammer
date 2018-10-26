package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class InvalidTokenException extends AbstractException {

    public InvalidTokenException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
