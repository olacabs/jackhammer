package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScanTool extends AbstractModel {
    private Long scanId;
    private Long toolId;
    private Long toolInstanceId;
    private String status;
}
