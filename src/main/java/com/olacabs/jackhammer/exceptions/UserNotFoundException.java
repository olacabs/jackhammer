package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class UserNotFoundException extends AbstractException {
    public UserNotFoundException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
