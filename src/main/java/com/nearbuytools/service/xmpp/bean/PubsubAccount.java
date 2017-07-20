package com.nearbuytools.service.xmpp.bean;

import java.io.Serializable;

import org.jxmpp.jid.parts.Localpart;

public class PubsubAccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2335443127203673152L;
	private Localpart username;
	private String password;
	private String hostName;
	private String domain;
	private int port;
	private boolean encryptPassword;
	

	public Localpart getUsername() {
		return username;
	}

	public void setUsername(Localpart username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isEncryptPassword() {
		return encryptPassword;
	}

	public void setEncryptPassword(boolean encryptPassword) {
		this.encryptPassword = encryptPassword;
	}

}
