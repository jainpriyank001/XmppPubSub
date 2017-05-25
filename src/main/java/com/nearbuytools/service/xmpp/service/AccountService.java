package com.nearbuytools.service.xmpp.service;

import java.io.IOException;

import javax.security.sasl.SaslException;

import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nearbuytools.service.xmpp.bean.Account;
import com.nearbuytools.service.xmpp.bean.AccountResponse;
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
	private XmppManagerBabbler xmppManager;

	public AccountResponse createAccount(Account account) throws XmppException, SaslException, IOException {
		// if(StringUtils.isBlank(account.getUserName()))
		AccountResponse res = new AccountResponse();
		if (account.isEncryptPassword()) {
			account.setPassword(EncryptionUtil.encrypt(account.getPassword()));
			res.setPassword(account.getPassword());
		}
		
		try {
			xmppManager.createAccount(account.getUserName(), account.getPassword());
		} catch (XmppException e) {
			/*if (e != null && e.getXMPPError() != null) {
				if (e.getXMPPError().getCode() == ACCOUNT_CONFLICT_CODE && !account.isOverride()) {
					throw e;
				}
			} else {
				throw e;
			}*/
			throw e;
		}

		/*new Thread(new Runnable() {
			public void run() {
				try {
					xmppManager.createAccount(account.getUserName(), account.getPassword());
				} catch (XMPPException e) {
					if (e != null && e.getXMPPError() != null) {
						if (e.getXMPPError().getCode() == ACCOUNT_CONFLICT_CODE && !account.isOverride()) {
							LOGGER.error("XMPPException while creating account for user " + account.getUserName() + ":" + e.getMessage(), e);
						}
					} else {
						LOGGER.error("Error while creating account for user " + account.getUserName() + ":" + e.getMessage(), e);
					}
				} catch (SaslException e) {
					LOGGER.error("SaslException while creating account for user " + account.getUserName() + ":" + e.getMessage(), e);
				} catch (IOException e) {
					LOGGER.error("IOException while creating account for user " + account.getUserName() + ":" + e.getMessage(), e);
				}
			}
		}).start();*/

		res.setJid(account.getUserName() + "@" + server);
		return res;
	}
}