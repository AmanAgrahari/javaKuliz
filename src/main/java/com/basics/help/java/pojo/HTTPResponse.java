package com.basics.help.java.pojo;

import java.io.InputStream;

public class HTTPResponse {

	private int statusCode;
	private String response;

	public HTTPResponse() {
		super();
	}

	public HTTPResponse(int statusCode, String response) {
		this.setStatusCode(statusCode);
		this.setResponse(response);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public InputStream getRawBody() {
		// TODO Auto-generated method stub
		return null;
	}

}