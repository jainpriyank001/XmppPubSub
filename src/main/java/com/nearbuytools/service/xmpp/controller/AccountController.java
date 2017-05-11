package com.nearbuytools.service.xmpp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nearbuytools.service.xmpp.bean.Account;
import com.nearbuytools.service.xmpp.bean.ErrorResponse;
import com.nearbuytools.service.xmpp.manager.XmppManager;
import com.nearbuytools.service.xmpp.util.ResponseUtil;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private XmppManager xmppManager;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> createAccount(@RequestBody(required=true) Account account,
			HttpServletRequest request, HttpServletResponse httpResponse) {
		HttpHeaders headers = new HttpHeaders();
		try {
			//if(StringUtils.isBlank(account.getUserName()))
				
			xmppManager.createAccount(account.getUserName(), account.getPassword());
			return ResponseUtil.sendResponse("account created successfully", headers, HttpStatus.OK);
			
		} catch (XMPPException e) {
			LOGGER.error("XMPPException while creating account for " + account.getUserName(), e);
            return ResponseUtil.sendResponse(new ErrorResponse(601, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
        	LOGGER.error("Exception while creating account for " + account.getUserName(), e);
            return ResponseUtil.sendResponse(new ErrorResponse(602, "Exception while creating account"), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}