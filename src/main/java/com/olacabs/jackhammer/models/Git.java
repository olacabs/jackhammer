package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Git extends AbstractModel {
    private String gitType;
    private String userName;
    private String gitEndPoint;
    private String apiAccessToken;
    private String organizationName;
}


