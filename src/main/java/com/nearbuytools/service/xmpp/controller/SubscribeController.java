package com.nearbuytools.service.xmpp.controller;

import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nearbuytools.service.xmpp.bean.ChatResponse;
import com.nearbuytools.service.xmpp.bean.ErrorResponse;
import com.nearbuytools.service.xmpp.manager.XmppConnectionManager;
import com.nearbuytools.service.xmpp.util.ResponseUtil;

@RestController
@RequestMapping(value="/api/sub")
public class SubscribeController {
	
		private static Logger LOGGER=LoggerFactory.getLogger(SubscribeController.class);
		
		@Autowired
		XmppConnectionManager xmppManager;
		
		@ApiOperation(value="Subscribe/unsubscribe a node",
				notes="User subscribes/unsubscibes to a node using JID"
				)
		
		@RequestMapping(path="/subscribe/{nodeName}/{jid}",method=RequestMethod.GET)
		public HttpEntity<Object> subscribe(@PathVariable("nodeName") String nodeName, @PathVariable("jid") String jid){
			HttpHeaders headers=new HttpHeaders();
			try{
				xmppManager.subscribeNode(nodeName, jid);
				LOGGER.info(jid + " is now subscribed to node "+nodeName);
				return ResponseUtil.sendResponse(new ChatResponse("Subscription successful"), headers, HttpStatus.OK);
			}
			catch(Exception e){
				LOGGER.error("Exception while subscribing node " + nodeName, e);
	            return ResponseUtil.sendResponse(new ErrorResponse(702, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);			
			}
			
		}
		
		@RequestMapping(path="/unsubscribe/{nodeName}/{jid}",method=RequestMethod.GET)
		public HttpEntity<Object> unsubscribe(@PathVariable("nodeName") String nodeName, @PathVariable("jid") String jid){
			HttpHeaders headers=new HttpHeaders();
			try{
				xmppManager.unsubscribeNode(nodeName, jid);
				LOGGER.info(jid + " is now unsubscribed from the node "+nodeName);
				return ResponseUtil.sendResponse(new ChatResponse("Subscription successful"), headers, HttpStatus.OK);
			}
			catch(Exception e){
				LOGGER.error("Exception while unsubscribing node " + nodeName, e);
	            return ResponseUtil.sendResponse(new ErrorResponse(702, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);			
			}
			
		}
		
}
