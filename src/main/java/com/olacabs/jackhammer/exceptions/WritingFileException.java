package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class WritingFileException extends AbstractException {
    public WritingFileException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }
}
