package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScanType extends AbstractModel {
    private Boolean isStatic;
    private Boolean isWeb;
    private Boolean isWordpress;
    private Boolean isMobile;
    private Boolean isNetwork;
    private Boolean isHardCodeSecret;
}
