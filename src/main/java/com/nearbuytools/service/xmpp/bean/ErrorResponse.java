package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer errorCode;
    private String errorMsg;
    
	public ErrorResponse() {
		super();
	}

	public ErrorResponse(Integer errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
