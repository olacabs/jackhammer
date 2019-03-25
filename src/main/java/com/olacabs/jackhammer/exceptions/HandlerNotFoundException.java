package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class HandlerNotFoundException extends AbstractException {

    public HandlerNotFoundException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }

}
