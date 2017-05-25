package com.nearbuytools.service.xmpp.manager;

import java.util.concurrent.Future;
import java.util.function.Consumer;

//import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.ConnectionEvent;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.util.concurrent.AsyncResult;

@Component
public class XmppManagerBabbler {

	private static Logger LOGGER = LoggerFactory.getLogger(XmppManagerBabbler.class);

	private static final int packetReplyTimeout = 30000; // millis

	@Value("${xmpp.server}")
	private String server;

	@Value("${xmpp.port}")
	private int port;

	@Value("${admin.username}")
	private String adminUser;

	@Value("${admin.password}")
	private String adminPassword;

	private TcpConnectionConfiguration tcpConfiguration;
	private XmppClient xmppClient;
	
	public void init() throws XmppException {
		
		tcpConfiguration = TcpConnectionConfiguration.builder()
			    .hostname(server)
			    .port(port)
			    .secure(false)
			    .connectTimeout(packetReplyTimeout)
			    .keepAliveInterval(30)
			    .build();
		
		xmppClient = XmppClient.create(server, tcpConfiguration);

		
		LOGGER.info("Initializing connection to server {} port {}", server, port);

		xmppClient.connect();
		
		createAdminAccount();
		
		xmppClient.addConnectionListener(new MyConnectionListener());
		
	}

	public void performLogin(String username, String password) throws XmppException  {
		if (xmppClient != null && xmppClient.isConnected()) {
			// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			xmppClient.login(username, password);
		}
	}

	/*public void setStatus(boolean available, String status) throws SaslException, XMPPException, IOException {
		connect();

		Presence.Type type = available ? Type.available : Type.unavailable;
		Presence presence = new Presence(type);

		//presence.setStatus(status);
		//xmppClient.sendPacket(presence);

	}*/

	/*public void keepConnectionAlive() throws SaslException, XMPPException, IOException {
		LOGGER.info("Pinging server " + server);
		IQ req = new IQ() {
			public String getChildElementXML() {
				return PING_XML;
			}
		};
		req.setType(IQ.Type.GET);
		PacketFilter filter = new AndFilter(new PacketIDFilter(req.getPacketID()), new PacketTypeFilter(IQ.class));
		pingCollector = xmppClient.createPacketCollector(filter);
		xmppClient.sendPacket(req);
	}*/

	public void destroy() throws XmppException {
		if (xmppClient != null && xmppClient.isConnected()) {
			xmppClient.close();
		}
	}

	public void connect() throws XmppException {
		if (xmppClient == null) {
			init();
		}

		if (!xmppClient.isConnected()) {
			xmppClient.connect();
		}

		if (!xmppClient.isAuthenticated()) {
			performLogin(adminUser, adminPassword);
		}
	}

	public void sendMessage(String message, String buddyJID) throws XmppException  {
		LOGGER.info("Sending mesage '{}' to user {}", message, buddyJID);
		connect();

		Future<Void> result = xmppClient.send(new Message(Jid.of(buddyJID + "@" + server), Message.Type.CHAT, message));
		/*while(!result.isDone()) {
        	try {
        		LOGGER.info("Waiting for sending message.");
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
	}

	/*public void createEntry(String user, String name) throws Exception {
		LOGGER.info("Creating entry for buddy '{}' with name {}", user, name);
		connect();

		Roster roster = connection.getRoster();
		roster.createEntry(user, name, null);
	}*/

	public void createAccount(String username, String password) throws XmppException {
		LOGGER.info("Creating account for '{}'", username);
		connect();

		RegistrationManager registrationManager = xmppClient.getManager(RegistrationManager.class);
        registrationManager.setEnabled(true);

        Registration registration = Registration.builder()
                .username(username)
                .password(password)
                .build();
        AsyncResult<Void> result = registrationManager.register(registration);
        /*while(!result.isDone()) {
        	try {
        		LOGGER.info("Waiting for account creation.");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
	}

	private void createAdminAccount() throws XmppException {
		try {
			createAccount(adminUser, adminPassword);
		} catch (XmppException e) {
			LOGGER.info("Admin account already exists");
		}

		try {
			performLogin(adminUser, adminPassword);
		} catch (XmppException e) {
			LOGGER.info("Unable to login to admin account ", e);
			throw e;
		}
		
		
		/*RegistrationManager registrationManager = xmppClient.getManager(RegistrationManager.class);
        registrationManager.setEnabled(true);
        AsyncResult<Registration> registration = registrationManager.getRegistration();
        
        while(!registration.isDone()) {
        	
        }
        if(!registration.isRegistered()) {
            registration = Registration.builder()
                    .username(authUser.getUsername().toString())
                    .password(authUser.getPassword().toString())
                    .build();
            registrationManager.register(registration);
        }

        xmppClient.login(authUser.getUsername().toString(), authUser.getPassword().toString());*/
	}

	/*class MyMessageListener implements MessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			String from = message.getFrom();
			String body = message.getBody();
			LOGGER.info("Received message '{}' from {}", body, from);
		}
	}*/

	class MyConnectionListener implements Consumer<ConnectionEvent> {

		@Override
		public void accept(ConnectionEvent event) {
			// TODO Auto-generated method stub
			if(event.getType().equals(ConnectionEvent.Type.DISCONNECTED))
				LOGGER.info("Connection closed");
			else if(event.getType().equals(ConnectionEvent.Type.RECONNECTION_FAILED))
				LOGGER.info("Reconnection failed");
			else if(event.getType().equals(ConnectionEvent.Type.RECONNECTION_PENDING))
				LOGGER.info("Reconnection pending");
			else if(event.getType().equals(ConnectionEvent.Type.RECONNECTION_SUCCEEDED))
				LOGGER.info("Reconnection successful");
		}
	}
}