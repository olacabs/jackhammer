package com.olacabs.jackhammer.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.Configuration;


import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JackhammerConfiguration  extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    @JsonProperty("jwtConfiguration")
    private JwtConfiguration jwtConfiguration;

    @NotNull
    @JsonProperty("scanManagerConfiguration")
    private ScanMangerConfiguration scanMangerConfiguration;

    @NotNull
    @JsonProperty("toolManagerConfiguration")
    private ToolManagerConfiguration toolManagerConfiguration;

    @JsonProperty("marathonConfiguration")
    public MarathonConfiguration marathonConfiguration;

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @NotNull
    @JsonProperty("fileConfiguration")
    private FileConfiguration fileConfiguration;

    @NotNull
    @JsonProperty("gitConfiguration")
    private GitConfiguration gitConfiguration;

    @NotNull
    @JsonProperty("clientConfiguration")
    private ClientConfiguration clientConfiguration;

    @NotNull
    @JsonProperty("s3Configuration")
    private S3Configuration s3Configuration;

}
