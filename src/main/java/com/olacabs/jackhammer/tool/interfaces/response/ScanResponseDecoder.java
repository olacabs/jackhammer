package com.olacabs.jackhammer.tool.interfaces.response;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class ScanResponseDecoder implements Decoder.Text<Map> {

    public Map decode(String scanResponse) throws DecodeException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(scanResponse, new TypeReference<HashMap<String, String>>() {});
        } catch (Exception e) {
            log.info("Problem with Decoder: " + e.getMessage());
            return new HashMap();
        }
    }

    public boolean willDecode(String scanResponse) {
        try {
            JsonFactory factory = new JsonFactory();
            factory.createParser(new StringReader(scanResponse));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void init(EndpointConfig config) {
        log.info("Scan Response Decoder -init method called");
    }

    public void destroy() {
        log.info("Scan Response Decoder - destroy method called");
    }
}
