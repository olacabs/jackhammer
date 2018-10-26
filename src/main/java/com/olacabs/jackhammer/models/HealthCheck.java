package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mesosphere.marathon.client.utils.ModelUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HealthCheck {

    private Command command;
    private Integer gracePeriodSeconds;
    private Integer intervalSeconds;
    private Integer maxConsecutiveFailures;
    private Integer portIndex;
    private Integer timeoutSeconds;
    private boolean ignoreHttp1xx;
    private String path;
    private String protocol;

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
