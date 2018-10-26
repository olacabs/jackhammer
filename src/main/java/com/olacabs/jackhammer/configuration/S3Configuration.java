package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class S3Configuration {

    @NotNull
    @JsonProperty
    private String accessKey;

    @NotNull
    @JsonProperty
    private String secretKey;

    @NotNull
    @JsonProperty
    private String bucketName;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }
}
