package com.olacabs.jackhammer.exceptions;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractException extends Exception {
    private CustomErrorCodes code;
    public AbstractException(String message, Throwable t,CustomErrorCodes code) {
        super(message,t);
        this.code = code;
    }

}
