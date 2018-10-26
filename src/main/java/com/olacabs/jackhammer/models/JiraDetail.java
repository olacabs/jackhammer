package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class JiraDetail extends AbstractModel {
    private String host;
    private String userName;
    private String password;
    private String defaultProject;
}
