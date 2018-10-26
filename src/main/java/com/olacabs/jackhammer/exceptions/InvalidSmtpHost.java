package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class InvalidSmtpHost extends AbstractException {
    public InvalidSmtpHost(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
