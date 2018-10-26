package com.olacabs.jackhammer.tool.interfaces.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.olacabs.jackhammer.models.Scan;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class ScanRequestEncoder implements Encoder.Text<Scan> {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	public String encode(Scan scan) throws EncodeException {
		 try {
	            return MAPPER.writeValueAsString(scan);
	        } catch (IOException e) {
	            throw new EncodeException(scan, "Could not encode.", e);
	        }
	}

}
