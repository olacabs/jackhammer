package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mesosphere.marathon.client.utils.ModelUtils;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PortMapping {
    private String protocol;
    private Integer hostPort;
    private Integer containerPort;

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
