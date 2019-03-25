package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitHubGroup {
    private String id;
    private String description;
    private String name;
    private String members_url;
    private String privacy;
    private String permission;
    private String slug;
    private String repositories_url;
    private String url;
    private String node_id;
    private String html_url;
    List<GitHubProject> gitHubProjects;
}
