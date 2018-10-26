package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeverityCount {
    private long totalCount;
    private long criticalCount;
    private long highCount;
    private long mediumCount;
    private long lowCount;
    private long infoCount;
}
