package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mesosphere.marathon.client.utils.ModelUtils;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Docker {
    private List<PortMapping> portMappings;
    private String image;
    private String network;
    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
