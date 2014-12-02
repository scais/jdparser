package uk.azdev.openfire.relay;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.common.InvalidConfigurationException;
import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.ReceivedInvitation;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.conversations.ConversationLogLine;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;

public class OpenFireRelay implements ConnectionEventListener {

	private XFireConnection connection;
	private CountDownLatch exitLatch;
	private Set<String> adminList;
	private CommandProcessor commandProcessor;
	
	public static final Logger relayLog = Logger.getLogger("relay");
	
	public OpenFireRelay(OpenFireConfiguration config, Set<String> adminList) {
		exitLatch = new CountDownLatch(1);
		connection = new XFireConnection(config);
		this.adminList = adminList;
		commandProcessor = new CommandProcessor();
	}
	
	private void start() throws UnknownHostException, IOException {
		connection.addListener(this);
		connection.blockingConnect();
	}
	
	public void waitForExit() throws InterruptedException {
		exitLatch.await();
	}

	public void connectionError() {
		System.err.println("A connection error occurred. The program will now terminate");
		exitLatch.countDown();
	}

	public void conversationUpdate(SessionId sessionId) {
		FriendsList list = connection.getFriendList();
		
		Friend sourceFriend = list.getOnlineFriend(sessionId);
		relayLog.fine("message received from " + sourceFriend.getUserName() + " (" + sessionId + ")");
		Conversation sourceConv = connection.getConversation(sessionId);
		ConversationLogLine message = sourceConv.getLastMessage();
		
		if(message.getMessage().startsWith("@")) {
			commandProcessor.processCommand(message.getMessage().substring(1), message.getOriginator(), connection);
		}else if(adminList.contains(sourceFriend.getUserName())) {
			relayLog.fine("Routing admin message");
			routeToAll(sourceFriend, message.toString());
		} else {
			relayLog.fine("Routing user message to admins");
			routeToAdmins(sourceFriend, message.toString());
		}
	}

	private void routeToAll(Friend source, String message) {
		FriendsList list = connection.getFriendList();
		Set<Friend> myFriends = list.getMyFriends();
		
		for(Friend f : myFriends) {
			if(!f.isOnline() || f.equals(source)) {
				continue;
			}
			
			relayLog.finer("Forwarding message to " + f.getDisplayName());
			
			Conversation conv = connection.getConversation(f.getSessionId());
			conv.sendMessage(message);
		}
	}
	
	private void routeToAdmins(Friend source, String message) {
		FriendsList list = connection.getFriendList();
		Set<Friend> myFriends = list.getMyFriends();
		
		for(Friend f : myFriends) {
			if(!f.isOnline() || f.equals(source) || !adminList.contains(f.getUserName())) {
				continue;
			}
			
			relayLog.finer("Forwarding message to " + f.getDisplayName());
			
			Conversation conv = connection.getConversation(f.getSessionId());
			conv.sendMessage("[" + source.getUserName() + "] " + message);
		}
	}

	public void disconnected() {
		relayLog.info("Client disconnected");
	}

	public void friendsListUpdated() {
		// don't need to do anything
	}

	public void internalError(Exception e) {
		System.err.println("An internal error occurred. The program will now terminate");
		e.printStackTrace();
		exitLatch.countDown();
	}

	public void loginFailed() {
		System.err.println("Incorrect authentication details provided.");
		exitLatch.countDown();
	}
	
	public static void main(String[] args) {
		try {
			configureLogging();
		} catch (Exception e) {
			System.err.println("Error occurred while configuring logging");
			e.printStackTrace();
			return;
		}
		
		OpenFireConfiguration config;
		try {
			config = readConfig();
		} catch (IOException e) {
			System.err.println("Unable to read configuration file: " + e.getMessage());
			return;
		} catch (InvalidConfigurationException e) {
			System.err.println("Configuration file is invalid: " + e.getMessage());
			return;
		}
		
		Set<String> admins;
		try {
			admins = parseAdminList();
		} catch (IOException e) {
			System.err.println("Unable to read admin list file: " + e.getMessage());
			return;
		}
		
		OpenFireRelay relay = new OpenFireRelay(config, admins);
		try {
			relay.start();
		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve host: " + e.getMessage());
			return;
		} catch (IOException e) {
			System.err.println("I/O error occurred: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		try {
			relay.waitForExit();
		} catch (InterruptedException e) {
			System.err.println("Main thread unexpectedly terminated");
		}
	}

	private static void configureLogging() throws SecurityException, IOException {
		Logging.connectionLogger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		Logging.connectionLogger.addHandler(new FileHandler());
		Logging.connectionLogger.setLevel(Level.FINEST);
		relayLog.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		relayLog.addHandler(new FileHandler());
		relayLog.setLevel(Level.FINEST);
	}

	private static OpenFireConfiguration readConfig() throws IOException, InvalidConfigurationException {
		FileInputStream reader = new FileInputStream("openfire.cfg");
		return OpenFireConfiguration.readConfig(reader);
	}

	private static Set<String> parseAdminList() throws IOException {
		
		FileInputStream reader = new FileInputStream("admin.list");
		Properties admins = new Properties();
		admins.load(reader);
		
		Set<Object> adminUids = admins.keySet();
		Set<String> adminUidSet = new HashSet<String>();
		
		for(Object uid : adminUids) {
			adminUidSet.add((String)uid);
		}
		
		return adminUidSet;
	}

	public void inviteReceived(ReceivedInvitation invite) {
		relayLog.info("Accepting invite from user \"" + invite.getUserName() + "\"");
		invite.accept();
	}
}
