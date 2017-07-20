package com.nearbuytools.service.xmpp.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.annotations.ApiOperation;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nearbuytools.service.xmpp.manager.XmppConnectionManager;

@RestController
@RequestMapping(value="api/readnode")

public class ReadNodeController {
	
	@SuppressWarnings("unused")
	private static Logger LOGGER= LoggerFactory.getLogger(ReadNodeController.class);
	
	@Autowired
	XmppConnectionManager xmppManager;
	
	@ApiOperation(value="To read payloads on a node")
	
	@RequestMapping(path="{nodeName}",method=RequestMethod.GET)
	public @ResponseBody List<Item> readNode(@PathVariable String nodeName) throws InterruptedException, XMPPException, IOException, SmackException{
		
		List<Item> values=xmppManager.readItemsOnNode(nodeName);
		return values;

	}

}
