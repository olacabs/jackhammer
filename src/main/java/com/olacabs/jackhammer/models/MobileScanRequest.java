package com.olacabs.jackhammer.models;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class MobileScanRequest extends File {
    private String scanId;
    public MobileScanRequest(String pathname) {
        super(pathname);
    }

}
