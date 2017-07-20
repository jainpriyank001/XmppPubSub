package com.nearbuytools.service.xmpp.manager;

import java.io.IOException;
import java.util.List;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.jivesoftware.smackx.pubsub.Subscription;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.parts.Localpart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nearbuytools.service.xmpp.bean.PubsubAccount;

import rocks.xmpp.core.XmppException;


@Component
public class XmppConnectionManager {

	static AbstractXMPPConnection conn1;
	static PubSubManager mgr;
	PubsubAccount account=new PubsubAccount();
	private static Logger LOGGER = LoggerFactory.getLogger(XmppManagerBabbler.class);

	
	@Value("${xmpp.server}")
	private String server;

	@Value("${xmpp.port}")
	private int port;

	@Value("${admin.username}")
	private Localpart adminUser;

	@Value("${admin.password}")
	private String adminPassword;


	//public static void main(String args[]) throws SmackException, IOException, XMPPException, InterruptedException{
	public void init() throws XMPPException, IOException, SmackException, InterruptedException {
		    
				account.setHostName("localhost");
				account.setUsername(adminUser);
				account.setPassword(adminPassword);
				account.setDomain(server);

				
				XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				.allowEmptyOrNullUsernames()
                .setXmppDomain(account.getDomain())
                .setHost(account.getHostName())
                //.setUsernameAndPassword(account.getUsername(), account.getPassword())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(port)
                .setDebuggerEnabled(true)
                .build();
		
				conn1=new XMPPTCPConnection(config);
        		conn1.connect();
        		//conn1.login();
        		//conn1.login(account.getUsername(), account.getPassword());
        		
//        		SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
//    		    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
//    		    SASLAuthentication.unBlacklistSASLMechanism("PLAIN");    		   				
	}
	
	public void performLogin(Localpart username, String password) throws XMPPException, SmackException, IOException, InterruptedException  {
		if (conn1 != null && conn1.isConnected()) {
			conn1.login(username, password);

		}
	}

	public void connect() throws XMPPException, IOException, SmackException, InterruptedException {
		if (conn1 == null) {
			init();
		}

//		if (!conn1.isConnected()) {
//			conn1.connect();
//		}

		if (!conn1.isAuthenticated()) {
			performLogin(adminUser, adminPassword);
		}
	}
	
	public void createAccount(Localpart username, String password) throws XmppException, XMPPException, IOException, SmackException, InterruptedException {
			LOGGER.info("Creating account for '{}'", username);
			connect();
			
			AccountManager accountManager=AccountManager.getInstance(conn1);
			accountManager.createAccount(username, password);
			
			performLogin(username,password);
	}

	 public void createNewNode(String nodeName) throws XMPPException, IOException, SmackException, InterruptedException{
		 
		 	LOGGER.info("Creating a new node "+nodeName);
			connect();

		 	mgr=PubSubManager.getInstance(conn1);
	    	LeafNode leaf;

	    	try {
	    		leaf = mgr.createNode(nodeName);
	    		ConfigureForm form = new ConfigureForm(DataForm.Type.submit);
	    		form.setAccessModel(AccessModel.open);
	    		form.setDeliverPayloads(true);
	    		form.setNotifyRetract(true);
	    		form.setPersistentItems(true);
	    		form.setPublishModel(PublishModel.open);
	    		leaf.sendConfigurationForm(form);

	    	} 
	    	catch (Exception e) {
	    		e.printStackTrace();
	    	} 
	 }
	 
 
	 
	public void subscribeNode(String nodeName, String jid) throws InterruptedException, XMPPException, IOException, SmackException{
		
		LOGGER.info(jid+" is subscribing to node "+nodeName);
		connect();

		mgr=PubSubManager.getInstance(conn1);
		LeafNode node = mgr.getNode(nodeName);
		node.subscribe(jid);
	}

	 public void unsubscribeNode(String nodeName, String jid) throws InterruptedException, XMPPException, IOException, SmackException{
		 	
		 	LOGGER.info(jid+" is unsubscribing node "+nodeName);
			connect();
		 
			mgr=PubSubManager.getInstance(conn1);
			LeafNode node = mgr.getNode(nodeName);
			node.unsubscribe(jid);
	}
	
	public void publishOnNode(String nodeName, String data) throws InterruptedException, XMPPException, IOException, SmackException{
		
		LOGGER.info("Publishing on node "+nodeName);
		connect();
		
		mgr=PubSubManager.getInstance(conn1);
		LeafNode node = mgr.getNode(nodeName);
		node.publish(new PayloadItem("nearbuy" + System.currentTimeMillis(), new SimplePayload("nearbuy", "pubsub:test:nearbuy","<nearbuy xmlns='pubsub:test:nearbuy'> <title>"+data+"</title> </nearbuy>")));
	}
	
	 public List<Item> readItemsOnNode(String nodeName) throws InterruptedException, XMPPException, IOException, SmackException{
		
		LOGGER.info("Read payloads on node "+nodeName);
		connect(); 
		
		mgr=PubSubManager.getInstance(conn1);
		LeafNode node = mgr.getNode(nodeName);
		
		//to print the payload items
		int i=0;
		List<Item> l1=node.getItems();
		while(i<l1.size()){
			System.out.println(l1.get(i++));
		}
		
		return l1;

	}
	
	 public List<Subscription> getSubscribersList() throws InterruptedException, XMPPException, IOException, SmackException{
    
		 	LOGGER.info("Getting the list of subscribers");
			connect();

			mgr=PubSubManager.getInstance(conn1);
    		List<Subscription> sub =mgr.getSubscriptions();
    		return sub; 
    		
//			to print subscriptions
//			int i=0;  		
//    		while(i<sub.size()){
//    			System.out.println(sub.get(i++));
//    		}
    			
    }
}
