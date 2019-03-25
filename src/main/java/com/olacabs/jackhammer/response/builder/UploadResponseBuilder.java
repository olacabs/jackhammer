package com.olacabs.jackhammer.response.builder;

import com.olacabs.jackhammer.models.Upload;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Slf4j
public class UploadResponseBuilder extends AbstractResponseBuilder<Upload> {

    @Override
    public Response buildFetchRecordResponse(Upload upload) {
        return Response.ok(upload)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + "\"" + upload.getName() + "\"")
                .build();
    }
}
