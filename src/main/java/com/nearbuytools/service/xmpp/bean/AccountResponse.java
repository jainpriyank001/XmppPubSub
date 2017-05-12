package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

public class AccountResponse implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4920933372724895858L;
	
	private String jid;
	private String password;
	
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
