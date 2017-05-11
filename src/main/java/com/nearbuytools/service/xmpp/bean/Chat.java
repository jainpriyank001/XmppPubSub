package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

public class Chat implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6530234600496706894L;
	private String from;
	private String to;
	private String message;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}