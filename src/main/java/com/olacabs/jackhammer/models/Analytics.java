package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Analytics extends AbstractModel {
    SeverityCount severityCount;
    private long runningScans;
    private long queuedScans;
    private long totalScans;
    private long completedScans;
    private long newFindings;
    private long userId;
    private List<Repo> repoList = new ArrayList<Repo>();
    private List<TopVulnerableType> topVulnerableTypes = new ArrayList<TopVulnerableType>();
}
