package com.olacabs.jackhammer.models;

import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;

@Getter
@Setter
public class Upload extends AbstractModel {
    long findingId;
    long userId;
    String userName;
    private InputStream uploadedInputStream;
    private FormDataContentDisposition fileDetail;
}
