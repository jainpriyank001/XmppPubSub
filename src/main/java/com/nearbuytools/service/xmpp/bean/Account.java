package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

public class Account implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1703220920863574215L;
	
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
