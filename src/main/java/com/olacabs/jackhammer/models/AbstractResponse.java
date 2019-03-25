package com.olacabs.jackhammer.models;

import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.HttpResponseCodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractResponse {

    private String message;
    private int successCode;
    private int errorCode;

    public void setErrorCode(CustomErrorCodes code) {
        this.errorCode = code.getValue();
    }

    public void setSuccessCode(int code) {
        this.successCode = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode(){
        return errorCode;
    }
    public int getSuccessCode(){
        return errorCode;
    }

    public String getMessage(){
        return message;
    }
}
