package com.mybank.app.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class JSONUtil {

	public JSONObject getJsonObjectFromObject(Object object) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			String result = mapper.writeValueAsString(object);
			return new JSONObject(result);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
}
