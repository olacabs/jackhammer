package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GitNamespace extends AbstractModel {
    @JsonIgnore
    private String path;
    @JsonIgnore
    private String kind;
    @JsonIgnore
    private String fullPath;
    @JsonIgnore
    private String owner_id;
    @JsonIgnore
    private String created_at;
    @JsonIgnore
    private String updated_at;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private boolean share_with_group_lock;
    @JsonIgnore
    private long visibility_level;
    @JsonIgnore
    private boolean request_access_enabled;
    @JsonIgnore
    private String deleted_at;
    @JsonIgnore
    private boolean lfs_enabled;
    @JsonIgnore
    private Map<String,String> avatar;
}
