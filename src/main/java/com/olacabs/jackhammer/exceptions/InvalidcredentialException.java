package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class InvalidcredentialException extends AbstractException {
    public InvalidcredentialException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
