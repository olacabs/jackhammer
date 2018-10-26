package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class MarathonNotRunning extends AbstractException {
    public MarathonNotRunning(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}


