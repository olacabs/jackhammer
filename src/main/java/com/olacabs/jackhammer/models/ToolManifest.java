package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ToolManifest {
    private String id;
    private Double cpus;
    private Container container;
    private Double mem;
    private Integer instances;
    private List<HealthCheck> healthChecks;
    private Map<String, String> env;
}
