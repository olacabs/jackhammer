package com.olacabs.jackhammer.configuration;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.websocket.server.ServerEndpointConfig.Configurator;


public class WebSocketsConfiguration extends Configurator {

    @Inject
    private static Injector injector;

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return injector.getInstance(endpointClass);
    }
}
