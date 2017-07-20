package com.nearbuytools.service.xmpp.service;

import java.io.IOException;

import javax.security.sasl.SaslException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nearbuytools.service.xmpp.bean.Account;
import com.nearbuytools.service.xmpp.bean.AccountResponse;
import com.nearbuytools.service.xmpp.bean.PubsubAccount;
import com.nearbuytools.service.xmpp.manager.XmppConnectionManager;
import com.nearbuytools.service.xmpp.manager.XmppManager;
import com.nearbuytools.service.xmpp.manager.XmppManagerBabbler;
import com.nearbuytools.service.xmpp.util.EncryptionUtil;

import rocks.xmpp.core.XmppException;

@Component
public class AccountService {

	@Value("${xmpp.server}")
	private String server;

	private static final int ACCOUNT_CONFLICT_CODE = 409;

	private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private XmppConnectionManager xmppManager;

	public AccountResponse createAccount(PubsubAccount account) throws XmppException, SaslException, IOException, XMPPException, SmackException, InterruptedException {
		
		AccountResponse res = new AccountResponse();
		
		if (account.isEncryptPassword()) {
			account.setPassword(EncryptionUtil.encrypt(account.getPassword()));
			res.setPassword(account.getPassword());
		}
		
		try {
			xmppManager.createAccount(account.getUsername(), account.getPassword());
		} catch (XmppException e) {
			throw e;
		}
		res.setJid(account.getUsername() + "@" + server);
		return res;
	}
}