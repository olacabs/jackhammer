package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Repo extends AbstractModel {
    private String target;
    private long groupId;
    private long userId;
    private long branchId;
    private long ownerTypeId;
    private long scanTypeId;
    private Group group;
    private List<Long> groupIds;
    private SeverityCount severityCount;
    private VulnerabilityTrend vulnerabilityTrend;
    private List<RepoToolResult> repoToolResults;
}
