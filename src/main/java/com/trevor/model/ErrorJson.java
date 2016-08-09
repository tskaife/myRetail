package com.trevor.model;

public class ErrorJson implements JsonObject {
	
	String error;

	public ErrorJson() {
		
	}
		
	public ErrorJson(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
