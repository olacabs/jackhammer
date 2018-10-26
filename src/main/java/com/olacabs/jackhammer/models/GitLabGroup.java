package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class GitLabGroup extends AbstractModel {
    @JsonIgnore
    private String visibility_level;
    @JsonIgnore
    private boolean lfs_enabled;
    @JsonIgnore
    private String avatar_url;
    @JsonIgnore
    private boolean request_access_enabled;
    @JsonIgnore
    private String path;
    @JsonIgnore
    private String description;

    private String web_url;
    List<GitLabProject> gitLabProjects = new ArrayList<GitLabProject>();
}
