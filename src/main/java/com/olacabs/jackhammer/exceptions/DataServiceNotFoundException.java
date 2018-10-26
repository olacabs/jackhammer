package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class DataServiceNotFoundException extends AbstractException {
    public DataServiceNotFoundException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
