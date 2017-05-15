package com.nearbuytools.service.xmpp.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.nearbuytools.service.xmpp.bean.Chat;
import com.nearbuytools.service.xmpp.exception.DataValidationException;

@Component
public class ChatValidator {
	
	public void validate(Chat chat) throws DataValidationException {
		if(StringUtils.isBlank(chat.getTo()))
			throw new DataValidationException("to is required");
		
		if(StringUtils.isBlank(chat.getMessage()))
			throw new DataValidationException("message is required");
	}
}