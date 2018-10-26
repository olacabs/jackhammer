package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class TempDirCreationException  extends AbstractException {
    public TempDirCreationException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
