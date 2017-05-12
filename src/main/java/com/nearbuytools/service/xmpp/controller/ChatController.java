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
import com.nearbuytools.service.xmpp.bean.Chat;
import com.nearbuytools.service.xmpp.bean.ChatResponse;
import com.nearbuytools.service.xmpp.bean.ErrorResponse;
import com.nearbuytools.service.xmpp.manager.XmppManager;
import com.nearbuytools.service.xmpp.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	private XmppManager xmppManager;
	
	@ApiOperation(value="Send a message to the given JSD", 
			notes="Service sends a chat message to the provided JID"
			)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> sendMessage(@RequestBody(required=true) Chat chat,
			HttpServletRequest request, HttpServletResponse httpResponse) {
		HttpHeaders headers = new HttpHeaders();
		try {
			//if(StringUtils.isBlank(account.getUserName()))
			//xmppManager.performLogin(chat.getFrom(), chat.getFrom());
			xmppManager.sendMessage(chat.getMessage(), chat.getTo());
			ChatResponse res = new ChatResponse();
			res.setMsg("message sent");
			return ResponseUtil.sendResponse(res, headers, HttpStatus.OK);
			
		} catch (XMPPException e) {
			LOGGER.error("XMPPException while sending messgae to " + chat.getTo(), e);
            return ResponseUtil.sendResponse(new ErrorResponse(701, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
        	LOGGER.error("Exception while sending messgae to " + chat.getTo(), e);
            return ResponseUtil.sendResponse(new ErrorResponse(702, "Exception while sending message"), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}