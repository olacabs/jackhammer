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
public class Dashboard extends AbstractModel  {
    private SeverityCount severityCount;
    private VulnerabilityTrend vulnerabilityTrend;
    private List<TopVulnerableType> topVulnerableTypes;
    private List<Repo> topVulnerableRepos;
    private Boolean isExecutiveDashboard;
    private long userId;
}
