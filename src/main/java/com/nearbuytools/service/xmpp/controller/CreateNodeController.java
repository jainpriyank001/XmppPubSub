package com.nearbuytools.service.xmpp.controller;

import io.swagger.annotations.ApiOperation;

//import org.jivesoftware.smack.XMPPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nearbuytools.service.xmpp.bean.ChatResponse;
import com.nearbuytools.service.xmpp.bean.ErrorResponse;
import com.nearbuytools.service.xmpp.manager.XmppConnectionManager;
import com.nearbuytools.service.xmpp.util.ResponseUtil;


@RestController
@RequestMapping(value="/api/createnode")

public class CreateNodeController {

	private static Logger LOGGER=LoggerFactory.getLogger(CreateNodeController.class);
	
	@Autowired
	private XmppConnectionManager xmppManager;
	
	@ApiOperation(value="Create a new node", 
			notes= "Node name is provided to create a new node"
			)
	
	
	@RequestMapping(method = RequestMethod.POST)
	public HttpEntity<Object>  createNode(@RequestBody(required=true) String nodeName) {
		HttpHeaders headers = new HttpHeaders();
		try{
			xmppManager.createNewNode(nodeName);
			LOGGER.info("Node created successfully", nodeName);
			return ResponseUtil.sendResponse(new ChatResponse("Node created"), headers, HttpStatus.OK);
		}
		catch (Exception e) {
        	LOGGER.error("Exception while creating node " + nodeName, e);
            return ResponseUtil.sendResponse(new ErrorResponse(702, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}
