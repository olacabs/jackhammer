package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JiraField {
    private String summary;
    private JiraProject project;
    private JiraIssueType issuetype;
    private String description;
}
