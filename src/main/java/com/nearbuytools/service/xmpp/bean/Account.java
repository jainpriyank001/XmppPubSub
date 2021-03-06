package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

public class Account implements Serializable {
	
	/**
	 * Using PubsubAccount.java instead of Account.java for Pubsub Implementation
	 */
	private static final long serialVersionUID = -1703220920863574215L;
	
	private String userName;
	private String password;
	private boolean encryptPassword;
	private boolean override;
	
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
	public boolean isEncryptPassword() {
		return encryptPassword;
	}
	public void setEncryptPassword(boolean encryptPassword) {
		this.encryptPassword = encryptPassword;
	}
	public boolean isOverride() {
		return override;
	}
	public void setOverride(boolean override) {
		this.override = override;
	}
}
