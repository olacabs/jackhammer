package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tool extends AbstractModel {
    private String manifestJson;
    private String status;
    private long instanceCount;
    private long languageId;
    InputStream uploadedInputStream;
    Boolean isEnabled;
}
