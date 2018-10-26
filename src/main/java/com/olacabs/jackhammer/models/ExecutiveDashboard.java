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
public class ExecutiveDashboard {
    private SeverityCount severityCount;
    private ScanTypeCount scanTypeCount;
    private List<Group> groups;
    private List<VulnerabilityTrend> criticalVulnerabilityTrend;
    private List<VulnerabilityTrend> highVulnerabilityTrend;
    private VulnerabilityTrend bugsClosingTrend;
}
