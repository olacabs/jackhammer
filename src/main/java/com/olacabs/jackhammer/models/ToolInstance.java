package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ToolInstance  extends AbstractModel {
    private long toolId;
    private long maxAllowedScans;
    private long currentRunningScans;
    private int port;
    private String status;
    private String sessionId;
    private String platform;
    private String containerId;
}
