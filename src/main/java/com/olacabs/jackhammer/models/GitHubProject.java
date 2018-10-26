package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GitHubProject  {
    private String has_issues;
    private String teams_url;
    private String compare_url;
    private String releases_url;
    private String keys_url;
    private String has_pages;
    private String description;
    private String milestones_url;
    private GitPermission permissions;
    private String has_wiki;
    private String events_url;
    private String archive_url;
    private String subscribers_url;
    private String contributors_url;
    private String pushed_at;
    private String fork;
    private String svn_url;
    private String collaborators_url;
    private String subscription_url;
    private String clone_url;
    private String trees_url;
    private String homepage;
    private String url;
    private String size;
    private String notifications_url;
    private String deployments_url;
    private String updated_at;
    private String branches_url;
    private GitOwner owner;
    private String issue_events_url;
    private String language;
    private String forks_count;
    private String contents_url;
    private String watchers_count;
    private String blobs_url;
    private String commits_url;
    private String has_downloads;
    private String git_commits_url;
    private String node_id;
    @JsonProperty("private")
    private String isPrivate;
    private String default_branch;
    private String open_issues;
    private String id;
    private String downloads_url;
    private String mirror_url;
    private String has_projects;
    private String archived;
    private String comments_url;
    private String name;
    private String created_at;
    private String stargazers_count;
    private String assignees_url;
    private String pulls_url;
    private String watchers;
    private String stargazers_url;
    private String hooks_url;
    private String languages_url;
    private String issues_url;
    private String git_tags_url;
    private String merges_url;
    private String git_refs_url;
    private String open_issues_count;
    private String ssh_url;
    private String html_url;
    private String forks;
    private String forks_url;
    private String statuses_url;
    private String issue_comment_url;
    private String labels_url;
    private String git_url;
    private String tags_url;
    private String full_name;

    @JsonIgnore
    private Map<String,String> license;
}
