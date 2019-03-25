package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class MarathonConfiguration {

    @NotNull
    @JsonProperty
    private String endpoint;

    @NotNull
    @JsonProperty
    private String jchAppId;

    public String getEndpoint() {
        return endpoint;
    }
    public String getJchAppId() {return jchAppId; }
}
