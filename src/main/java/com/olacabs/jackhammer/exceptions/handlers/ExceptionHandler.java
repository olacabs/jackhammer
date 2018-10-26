package com.olacabs.jackhammer.exceptions.handlers;

import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.models.ErrorResponseModel;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;

@Slf4j
public class ExceptionHandler {

    public Response handle(Throwable exception)  {
        ErrorResponseModel model = new ErrorResponseModel();
        model.setErrorCode(CustomErrorCodes.SERVICE_INTERNAL_EXCEPTION);
        model.setMessage(ExceptionMessages.INTERNAL_ERROR);
        log.error("Unknown error...", exception);
        return Response.status(CustomErrorCodes.INTERNAL_ERROR.getValue()).entity(model).build();
    }
}
