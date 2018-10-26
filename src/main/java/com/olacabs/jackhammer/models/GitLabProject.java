package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GitLabProject extends AbstractModel {

    @JsonIgnore
    private String default_branch;
    @JsonIgnore
    private String[] tag_list;
    @JsonIgnore
    private boolean archived;
    @JsonIgnore
    private String visibility_level;
    @JsonIgnore
    private String name_with_namespace;
    @JsonIgnore
    private String path_with_namespace;
    @JsonIgnore
    private boolean container_registry_enabled;
    @JsonIgnore
    private boolean  issues_enabled;
    @JsonIgnore
    private boolean merge_requests_enabled;
    @JsonIgnore
    private GitLabProject forked_from_project;
    @JsonIgnore
    private Integer star_count;
    @JsonIgnore
    private boolean wiki_enabled;
    @JsonIgnore
    private boolean builds_enabled;
    @JsonIgnore
    private boolean snippets_enabled;
    @JsonIgnore
    private String created_at;
    @JsonIgnore
    private String last_activity_at;
    @JsonIgnore
    private  String shared_runners_enabled;
    @JsonIgnore
    private boolean lfs_enabled;
    @JsonIgnore
    private  String creator_id;
    @JsonIgnore
    private GitNamespace namespace;
    @JsonIgnore
    private String avatar_url;
    @JsonIgnore
    private long start_count;
    @JsonIgnore
    private long forks_count;
    @JsonIgnore
    private long open_issues_count;
    @JsonIgnore
    private boolean public_builds;
    @JsonIgnore
    private List<Map<String,String>> shared_with_groups;
    @JsonIgnore
    private boolean only_allow_merge_if_build_succeeds;
    @JsonIgnore
    private boolean request_access_enabled;
    @JsonIgnore
    private boolean only_allow_merge_if_all_discussions_are_resolved;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private Map<String,String> owner;
    @JsonProperty("public")
    private Boolean isPublic;

    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private String web_url;
    private String path;
}
