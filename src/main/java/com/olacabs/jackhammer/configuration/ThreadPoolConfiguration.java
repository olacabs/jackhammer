package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ThreadPoolConfiguration {

    @NotNull
    @JsonProperty
    private Integer threadPoolSize;

    @NotNull
    @JsonProperty
    private Integer initialDelay;

    @NotNull
    @JsonProperty
    private Integer period;

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }

    public Integer getInitialDelay() {
        return initialDelay;
    }

    public Integer getPeriod() {
        return period;
    }
}
