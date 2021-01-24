package com.mybank.app.util;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ResponseParser implements Serializable {

	private static final long serialVersionUID = 1L;

	private int statusCode;
	private String developerMessage;
	private String userMessage;

	public ResponseParser build(int statusCode, String developerMessage, String userMessage) {
		this.statusCode = statusCode;
		this.developerMessage = developerMessage;
		this.userMessage = userMessage;
		return this;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	@Override
	public String toString() {
		return "ResponseParser [statusCode=" + statusCode + ", developerMessage=" + developerMessage + ", userMessage="
				+ userMessage + "]";
	}

}
