package com.olacabs.jackhammer.models;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Finding extends AbstractModel {
    private String severity;
    private String description;
    private String toolName;
    private String fileName;
    private String lineNumber;
    private String code;
    private String externalLink;
    private String solution;
    private String cvssScore;
    private String location;
    private String userInput;
    private String advisory;
    private String port;
    private String protocol;
    private String state;
    private String product;
    private String scripts;
    private String version;
    private String host;
    private String request;
    private String response;
    private String fingerprint;
    private String repoUrl;
    private String applicationName;
    private String status;
    private String cveCode;
    private String cweCode;
    private Boolean isFalsePositive;
    private Boolean notExploitable;
    private Boolean pushedToJira;
    private List<Long> ids;
    private Boolean bulkUpdate;
    private Boolean exportCSV;
    private String responseType;
    private String modifiedBy;

    //repo pages start
    private Boolean repoPage;
    private Boolean repoChartSummary;
    private Boolean repoFindingsPage;
    private Boolean repoToolResults;
    private Boolean readComments;
    private Boolean readUploads;
    private Boolean readTags;
    private Boolean createComments;
    private Boolean createUploads;
    private Boolean createTags;
    private Boolean updateFinding;


    //repo pages end
    private long userId;
    private long repoId;
    private long scanId;
    private long groupId;
    private long activePage;

    private List<Tag> tags = Lists.newArrayList();
    private List<String> tagNames = Lists.newArrayList();

    public void addTag(Tag t) {
        if (!tags.contains(t)) tags.add(t);
    }
}
