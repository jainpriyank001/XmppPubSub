package com.nearbuytools.service.xmpp.service;

import java.io.IOException;

import javax.security.sasl.SaslException;

import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nearbuytools.service.xmpp.bean.Account;
import com.nearbuytools.service.xmpp.bean.AccountResponse;
import com.nearbuytools.service.xmpp.manager.XmppManager;
import com.nearbuytools.service.xmpp.util.EncryptionUtil;

@Component
public class AccountService {

	@Value("${xmpp.server}")
    private String server;
	
	@Autowired
	private XmppManager xmppManager;
	
	public AccountResponse createAccount(Account account) throws XMPPException, SaslException, IOException {
		//if(StringUtils.isBlank(account.getUserName()))
		AccountResponse res = new AccountResponse();
		if(account.isEncryptPassword()) {
			account.setPassword(EncryptionUtil.encrypt(account.getPassword()));
			res.setPassword(account.getPassword());
		}
		System.out.println(account.getPassword());
		xmppManager.createAccount(account.getUserName(), account.getPassword());
		res.setJid(account.getUserName() + "@" + server);
		return res;
	}	
}