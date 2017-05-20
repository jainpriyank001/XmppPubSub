package com.nearbuytools.service.xmpp.manager;

import java.io.IOException;

import javax.security.sasl.SaslException;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
//import org.jivesoftware.smack.SmackException;
//import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XmppManager {
	
	private static Logger LOGGER = LoggerFactory.getLogger(XmppManager.class);
    
    private static final int packetReplyTimeout = 5000; // millis
    
    @Value("${xmpp.server}")
    private String server;
    
    @Value("${xmpp.port}")
    private int port;
    
    @Value("${admin.username}")
    private String adminUser;
    
    @Value("${admin.password}")
    private String adminPassword;
    
    private ConnectionConfiguration config;
    private XMPPConnection connection;

    private ChatManager chatManager;
    private MessageListener messageListener;
    
    public void init() throws XMPPException, SaslException, IOException {
        
    	LOGGER.info("Initializing connection to server {} port {}", server, port);

        SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
        
        config = new ConnectionConfiguration(server, port);
        config.setSASLAuthenticationEnabled(true);
        //config.setCompressionEnabled(false);
        config.setSecurityMode(SecurityMode.enabled);
        
        connection = new XMPPConnection(config);
        connection.connect();
         
        chatManager = connection.getChatManager();
        messageListener = new MyMessageListener();
        
        createAdminAccount();
    }
    
    public void performLogin(String username, String password) throws XMPPException, SaslException, IOException {
    	if (connection!=null && connection.isConnected()) {
        	//SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            connection.login(username, password);
        }
    }

    public void setStatus(boolean available, String status) throws SaslException, XMPPException, IOException {
        connect();
        
        Presence.Type type = available? Type.available: Type.unavailable;
        Presence presence = new Presence(type);
        
        presence.setStatus(status);
        connection.sendPacket(presence);
        
    }
    
    public void destroy()  {
        if (connection!=null && connection.isConnected()) {
            connection.disconnect();
        }
    }
    
    public void connect() throws XMPPException, SaslException, IOException  {
    	if(connection == null) {
    		init();
    	}
    	
        if (!connection.isConnected()) {
            connection.connect();
        }
        
        if(!connection.isAuthenticated()) {
        	performLogin(adminUser, adminPassword);
        }
    }
    
    public void sendMessage(String message, String buddyJID) throws XMPPException, SaslException, IOException {
    	LOGGER.info("Sending mesage '{}' to user {}", message, buddyJID);
    	connect();
        
        Chat chat = chatManager.createChat(buddyJID + "@" + server, messageListener);
        chat.sendMessage(message);
    }
    
    public void createEntry(String user, String name) throws Exception {
    	LOGGER.info("Creating entry for buddy '{}' with name {}", user, name);
    	connect();
    	
        Roster roster = connection.getRoster();
        roster.createEntry(user, name, null);
    }
    
    public void createAccount(String username, String password) throws XMPPException, SaslException, IOException {
    	LOGGER.info("Creating account for '{}'", username);
    	connect();
    	
    	AccountManager am = new AccountManager(connection);
    	am.createAccount(username, password);
    }
    
    private void createAdminAccount() throws XMPPException, IOException {
    	AccountManager am = new AccountManager(connection);
        try {
			am.createAccount(adminUser, adminPassword);
		} catch (XMPPException e) {
			LOGGER.info("Admin account already exists");
		}
        
        try {
			performLogin(adminUser, adminPassword);
		} catch (XMPPException | IOException e) {
			LOGGER.info("Unable to login to admin account ", e);
			throw e;
		}
    }
    
    class MyMessageListener implements MessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            String from = message.getFrom();
            String body = message.getBody();
            LOGGER.info("Received message '{}' from {}", body, from);
        }
    }
}