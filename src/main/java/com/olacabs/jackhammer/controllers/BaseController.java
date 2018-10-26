package com.olacabs.jackhammer.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.google.inject.Inject;

import com.olacabs.jackhammer.exceptions.handlers.ExceptionHandler;
import com.olacabs.jackhammer.handler.factories.HandlerFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public abstract class BaseController {

    @Inject
    private HandlerFactory handlerFactory;

    @Inject
    private ExceptionHandler exceptionHandler;

}
