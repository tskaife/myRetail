package com.trevor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static String convertObjectToJson(Object object){
		String json = null;
		try {
			json = MAPPER.writer().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
