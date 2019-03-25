package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class FileConfiguration {

    @NotNull
    @JsonProperty
    private String targetDirectory;

    @NotNull
    @JsonProperty
    float fileLimitSize;

    @NotNull
    @JsonProperty
    String toolsDir;

    public String getTargetDirectory() {
        return targetDirectory;
    }
    public float  getFileLimitSize() { return fileLimitSize;}
    public String  getToolsDir() { return toolsDir;}
}
