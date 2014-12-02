/*
 * OpenFire - a Java API to access the XFire instant messaging network.
 * Copyright (C) 2007 Iain McGinniss
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.azdev.openfire;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import uk.azdev.openfire.common.GameInfoMap;
import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.conversations.IConversationStore;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.friendlist.messageprocessors.ChatMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendGameInfoMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendListMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendOfFriendListMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendStatusMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.IMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.IncomingInvitationMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.LoginChallengeMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.LoginFailureMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.LoginSuccessMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.NewVersionAvailableMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.UserSessionIdListMessageProcessor;
import uk.azdev.openfire.net.ConnectionController;
import uk.azdev.openfire.net.ConnectionStateListener;
import uk.azdev.openfire.net.IConnectionController;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;
import uk.azdev.openfire.net.messages.incoming.FriendGameInfoMessage;
import uk.azdev.openfire.net.messages.incoming.FriendListMessage;
import uk.azdev.openfire.net.messages.incoming.FriendOfFriendListMessage;
import uk.azdev.openfire.net.messages.incoming.FriendStatusMessage;
import uk.azdev.openfire.net.messages.incoming.IncomingInvitationMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginFailureMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.incoming.NewVersionAvailableMessage;
import uk.azdev.openfire.net.messages.incoming.ServerRoutedChatMessage;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessage;
import uk.azdev.openfire.net.messages.outgoing.OutgoingInvitationMessage;

public class XFireConnection implements IMessageSender, ConnectionStateListener, ConnectionManipulator, IConversationStore {
	
	private OpenFireConfiguration config;
	private IConnectionController controller;
	private KeepaliveTimer timer;
	
	private Map<Integer,IMessageProcessor> processorMap;
	private FriendsList friendList;
	
	private Map<SessionId, Conversation> activeConversations;
	
	private ConnectionEventDispatcher eventDispatcher;
	private RawMessageDispatcher rawMessageDispatcher;
	
	private GameInfoMap gameInfoMap;
	
	protected XFireConnection(OpenFireConfiguration config, IConnectionController controller) {
		this.config = config;
		this.controller = controller;
		init();
	}
	
	public XFireConnection(OpenFireConfiguration config) {
		this.config = config;
		controller = new ConnectionController(config.getXfireServerHostName(), config.getXfireServerPortNum());
		init();
	}
	
	private void init() {
		friendList = new FriendsList(new Friend(config.getUsername()));
		
		controller.addStateListener(this);
		timer = new KeepaliveTimer(this);
		activeConversations = new ConcurrentHashMap<SessionId, Conversation>();
		eventDispatcher = new ConnectionEventDispatcher();
		rawMessageDispatcher = new RawMessageDispatcher();
		
		loadGameInfo();
		
		initProcessorMap();
	}
	
	private void loadGameInfo() {
		gameInfoMap = new GameInfoMap();
		InputStreamReader reader;
		try {
			File xfireGamesIniFile = new File(config.getXfireGamesIniPath());
			if(xfireGamesIniFile.exists() && xfireGamesIniFile.isFile()) {
				reader = new FileReader(xfireGamesIniFile);
				gameInfoMap.loadFromXfireGamesIni(reader);
			} else {
				Logging.connectionLogger.warning("Resource at path " + config.getXfireGamesIniPath() + " is not a valid xfire_games.ini file");
			}
		} catch(IOException e) {
			Logging.connectionLogger.warning("Unable to read xfire_games.ini from the following path: " + config.getXfireGamesIniPath());
		}
	}

	public void connect() {
		new Thread(new ConnectHandler()).start();
	}
	
	private class ConnectHandler implements Runnable {

		public void run() {
			try {
				blockingConnect();
			} catch (UnknownHostException e) {
				Logging.connectionLogger.log(Level.SEVERE, "The network address of xfire server could not be resolved", e);
			} catch (IOException e) {
				Logging.connectionLogger.log(Level.SEVERE, "An error occurred while trying to connect to the xfire server", e);
			}
		}
	}
	
	public void blockingConnect() throws UnknownHostException, IOException {
		controller.start();
		timer.start();
		sendClientInfo();
	}

	public void disconnect() {
		new Thread(new DisconnectHandler()).start();
	}
	
	private class DisconnectHandler implements Runnable {

		public void run() {
			try {
				blockingDisconnect();
			} catch (InterruptedException e) {
				Logging.connectionLogger.log(Level.SEVERE, "Thread was interrupted while waiting for disconnect to complete", e);
			} catch (IOException e) {
				Logging.connectionLogger.log(Level.SEVERE, "An error occurred while trying to connect to the xfire server", e);
			}
		}
	}
	
	public void blockingDisconnect() throws InterruptedException, IOException {
		timer.stop();
		controller.stop();
		eventDispatcher.disconnected();
	}
	
	public FriendsList getFriendList() {
		return friendList;
	}
	
	private void initProcessorMap() {
		processorMap = new HashMap<Integer, IMessageProcessor>();
		processorMap.put(FriendListMessage.FRIEND_LIST_MESSAGE_ID, new FriendListMessageProcessor(friendList, eventDispatcher));
		processorMap.put(FriendOfFriendListMessage.FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE, new FriendOfFriendListMessageProcessor(friendList));
		processorMap.put(FriendStatusMessage.FRIEND_STATUS_MESSAGE_ID, new FriendStatusMessageProcessor(friendList));
		processorMap.put(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID, new UserSessionIdListMessageProcessor(friendList, eventDispatcher));
		processorMap.put(LoginChallengeMessage.LOGIN_CHALLENGE_MESSAGE_ID, new LoginChallengeMessageProcessor(this, config));
		processorMap.put(LoginSuccessMessage.LOGIN_SUCCESS_MESSAGE_ID, new LoginSuccessMessageProcessor(this, friendList, config));
		processorMap.put(LoginFailureMessage.LOGIN_FAILURE_MESSAGE_ID, new LoginFailureMessageProcessor(this));
		processorMap.put(NewVersionAvailableMessage.TYPE_ID, new NewVersionAvailableMessageProcessor(this, config));
		processorMap.put(ServerRoutedChatMessage.SR_TYPE_ID, new ChatMessageProcessor(this, eventDispatcher, this));
		processorMap.put(ChatMessage.TYPE_ID, new ChatMessageProcessor(this, eventDispatcher, this));
		processorMap.put(IncomingInvitationMessage.TYPE_ID, new IncomingInvitationMessageProcessor(eventDispatcher, this));
		processorMap.put(FriendGameInfoMessage.FRIEND_GAME_INFO_MESSAGE_ID, new FriendGameInfoMessageProcessor(friendList, eventDispatcher, gameInfoMap));
	}
	
	private void sendClientInfo() {
		ClientInformationMessage clInfo = new ClientInformationMessage();
		clInfo.setVersion(config.getLongVersion());
		clInfo.setSkinList(config.getSkinList());
		sendMessage(clInfo);
		
		ClientVersionMessage clVersion = new ClientVersionMessage();
		clVersion.setVersion(config.getShortVersion());
		sendMessage(clVersion);
	}
	
	public void messageReceived(IMessage message) {
		if(processorMap.containsKey(message.getMessageId())) {
			processorMap.get(message.getMessageId()).processMessage(message);
		}
		
		rawMessageDispatcher.messageReceived(message);
	}
	
	public void connectionError(Exception e) {
		Logging.connectionLogger.log(Level.SEVERE, "Connection error occurred, client disconnected", e);
		new Thread(new ConnectionErrorHandler()).start();
	}

	public void loginFailed() {
		new Thread(new LoginFailureHandler()).start();
	}

	public void reconnect() {
		new Thread(new ReconnectHandler()).start();
	}
	
	private class ReconnectHandler implements Runnable {
		public void run() {
			try {
				blockingReconnect();
			} catch (Exception e) {
				Logging.connectionLogger.log(Level.SEVERE, "Error occurred while attempting to reconnect", e);
				connectionError(e);
			}
		}
	}
	
	public void blockingReconnect() throws InterruptedException, UnknownHostException, IOException {
		blockingDisconnect();
		blockingConnect();
	}
	
	public void sendMessage(IMessage message) {
		controller.sendMessage(message);
		timer.resetTimer();
	}

	public Conversation getConversation(SessionId peerSid) {
		
		if(!activeConversations.containsKey(peerSid)) {
			Friend peer = friendList.getOnlineFriend(peerSid);
			Conversation conversation = new Conversation(friendList, peer.getUserId(), this, config);
			activeConversations.put(peerSid, conversation);
		}
		
		return activeConversations.get(peerSid);
	}
	
	public void sendInvitation(String userName, String inviteMessage) {
		OutgoingInvitationMessage message = new OutgoingInvitationMessage();
		message.setUserName(userName);
		message.setMessage(inviteMessage);
		
		sendMessage(message);
	}
	
	public void addListener(ConnectionEventListener listener) {
		eventDispatcher.addListener(listener);
	}
	
	public void addRawListener(RawConnectionListener listener) {
	    rawMessageDispatcher.addRawListener(listener);
	}
	
	private class LoginFailureHandler implements Runnable {

		public void run() {
			try {
				blockingDisconnect();
			} catch (Exception e) {
				eventDispatcher.internalError(e);
			}
			
			eventDispatcher.loginFailed();
		}
	}
	
	private class ConnectionErrorHandler implements Runnable {
		
		public void run() {
			try {
				blockingDisconnect();
			} catch (Exception e) {
				eventDispatcher.internalError(e);
			}
			
			eventDispatcher.connectionError();
		}
	}
}
