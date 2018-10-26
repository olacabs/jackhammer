package com.olacabs.jackhammer.configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtConfiguration {

    @NotNull
    @JsonProperty
    private Integer tokenExpirationTime;

    @NotNull
    @JsonProperty
    private String tokenSigningKey;

    @NotNull
    @JsonProperty
    private Integer refreshTokenExpTime;

    @NotNull
    @JsonProperty
    private String tokenIssuer;



    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

}
