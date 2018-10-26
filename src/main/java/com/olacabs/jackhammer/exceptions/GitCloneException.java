package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;

public class GitCloneException  extends AbstractException {

    public GitCloneException(String message, Throwable t, CustomErrorCodes code) {
        super(message, t, code);
    }

}
