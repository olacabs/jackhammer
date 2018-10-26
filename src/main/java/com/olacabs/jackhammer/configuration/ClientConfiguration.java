package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ClientConfiguration {
    @NotNull
    @JsonProperty
    private String clientUrl;


    public String getClientUrl() {
        return clientUrl;
    }
}
