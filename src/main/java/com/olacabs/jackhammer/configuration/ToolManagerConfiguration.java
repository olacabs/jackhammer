package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ToolManagerConfiguration extends ThreadPoolConfiguration {
    @NotNull
    @JsonProperty
    private Boolean enableAutoScaling;

    public Boolean getEnableAutoScaling() {
        return enableAutoScaling;
    }
}
