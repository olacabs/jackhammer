package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class InvalidApplicationUrlException extends AbstractException {
    public InvalidApplicationUrlException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
