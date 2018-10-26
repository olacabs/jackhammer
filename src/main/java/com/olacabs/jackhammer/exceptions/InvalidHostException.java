package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class InvalidHostException extends AbstractException {
    public InvalidHostException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
