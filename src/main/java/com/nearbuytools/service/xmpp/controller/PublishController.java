package com.nearbuytools.service.xmpp.controller;

import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nearbuytools.service.xmpp.bean.ChatResponse;
import com.nearbuytools.service.xmpp.bean.ErrorResponse;
import com.nearbuytools.service.xmpp.manager.XmppConnectionManager;
import com.nearbuytools.service.xmpp.util.ResponseUtil;



@RestController
@RequestMapping(value="api/publish")
public class PublishController {
	
		private static Logger LOGGER=LoggerFactory.getLogger(PublishController.class);
		
		
		@Autowired
		private XmppConnectionManager xmppManager;
		
		@ApiOperation(value="Publish on a node",
				notes="Publish payload data on the node"
				)
		
		
		@RequestMapping(path="/{nodeName}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
		public HttpEntity<Object> publishData(@PathVariable("nodeName") String nodeName, @RequestBody(required=true) String data){
			HttpHeaders headers=new HttpHeaders();
			try{
				xmppManager.publishOnNode(nodeName, data);
				LOGGER.info("Data published successfully", nodeName);
				return ResponseUtil.sendResponse(new ChatResponse("Data Published on Node"), headers, HttpStatus.OK);
			}
			catch(Exception e){
				LOGGER.error("Exception while publishing on node " + nodeName, e);
	            return ResponseUtil.sendResponse(new ErrorResponse(702, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
}
