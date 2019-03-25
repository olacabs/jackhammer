package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class GitConfiguration extends ThreadPoolConfiguration {
    @NotNull
    @JsonProperty
    private String internalUrl;

    @NotNull
    @JsonProperty
    private String externalUrl;

    public String getExternalUrl() {
        return externalUrl;
    }

    public String getInternalUrl() {
        return internalUrl;
    }
}
