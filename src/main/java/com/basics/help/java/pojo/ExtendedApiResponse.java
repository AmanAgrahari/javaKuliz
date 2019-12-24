package com.basics.help.java.pojo;



public class ExtendedApiResponse extends ApiResponse {

	private String screenInfo;
	private String screenName;
	private String errorMessage;

	public ExtendedApiResponse(HttpStatus status, String message, Object data, String screenInfo, String screenName,
			String errorMessage) {
		super(status, message, data);
		this.screenName = screenName;
		this.screenInfo = screenInfo;
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getScreenInfo() {
		return screenInfo;
	}

	public void setScreenInfo(String screenInfo) {
		this.screenInfo = screenInfo;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}