package com.olacabs.jackhammer.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.ToolManifest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ToolUtil {

    public ToolManifest buildToolManifestRecord(Tool tool) {
        ToolManifest toolManifest = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String manifest = tool.getManifestJson();
            toolManifest = mapper.readValue(manifest, ToolManifest.class);
            toolManifest.getEnv().put(Constants.TOOL_ID,String.valueOf(tool.getId()));
            if(toolManifest.getContainer().getVolumes()!=null && toolManifest.getContainer().getVolumes().size() > 0) {
                toolManifest.getEnv().put(Constants.CONTAINER_VOLUME_PATH,toolManifest.getContainer().getVolumes().get(0).getContainerPath());
            }
            return toolManifest;
        } catch (IOException io) {
            log.error("Error while reading tool manifest info json",io);
        }
        return toolManifest;
    }
}
