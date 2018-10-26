package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScanTypeCount {
    private SeverityCount sourceCodeCount;
    private SeverityCount wordpressCount;
    private SeverityCount networkCount;
    private SeverityCount mobileCount;
    private SeverityCount webCount;
}
