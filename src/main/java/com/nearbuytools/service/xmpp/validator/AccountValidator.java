package com.nearbuytools.service.xmpp.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.nearbuytools.service.xmpp.bean.Account;
import com.nearbuytools.service.xmpp.exception.DataValidationException;

@Component
public class AccountValidator {
	
	public void validate(Account account) throws DataValidationException {
		if(StringUtils.isBlank(account.getUserName()))
			throw new DataValidationException("username is required");
		
		if(StringUtils.isBlank(account.getPassword()))
			throw new DataValidationException("password is required");
	}
}