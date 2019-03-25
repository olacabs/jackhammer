package com.olacabs.jackhammer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ScanMangerConfiguration extends ThreadPoolConfiguration {

    @NotNull
    @JsonProperty
    private String  alertMails;

    @NotNull
    @JsonProperty
    private Boolean  wpScanAlerts;

    @NotNull
    @JsonProperty
    private Boolean  webScanAlerts;

    @NotNull
    @JsonProperty
    private Boolean  staticCodeScanAlerts;


    @NotNull
    @JsonProperty
    private Boolean  hardcodeSecretScanAlerts;

    @NotNull
    @JsonProperty
    private Boolean  mobileScanAlerts;


    @NotNull
    @JsonProperty
    private Boolean  networkScanAlerts;

    public String getAlertMails() {
        return alertMails;
    }

    public Boolean getStaticCodeScanAlerts() {
        return staticCodeScanAlerts;
    }

    public Boolean getHardcodeSecretScanAlerts() {
        return hardcodeSecretScanAlerts;
    }

    public Boolean getNetworkScanAlerts() {
        return networkScanAlerts;
    }

    public Boolean getMobileScanAlerts() {
        return mobileScanAlerts;
    }

    public Boolean getWebScanAlerts() {
        return webScanAlerts;
    }

    public Boolean getWpScanAlerts() {
        return wpScanAlerts;
    }
}
