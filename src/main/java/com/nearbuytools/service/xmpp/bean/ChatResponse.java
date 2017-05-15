package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

public class ChatResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5627365280509202669L;

	private String msg;

	public ChatResponse() {
		super();
	}
	public ChatResponse(String msg) {
		super();
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}