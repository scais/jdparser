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
package uk.azdev.openfire.conversations;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.ProtocolConstants;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class Conversation implements IConversation {
	
	private IMessageSender messageSender;
	private FriendsList friendsList;
	private long peerUid;
	
	private int myMessageIndex;
	private LinkedList<ConversationLogLine> chatLog;
	
	private long lastClientInfoTransmitTime;
	
	private List<IConversationListener> listeners;
	
	private OpenFireConfiguration config;
	
	public Conversation(FriendsList friendsList, long peerUid, IMessageSender messageSender, OpenFireConfiguration config) {
		this.friendsList = friendsList;
		this.peerUid = peerUid;
		this.messageSender = messageSender;
		this.chatLog = new LinkedList<ConversationLogLine>();
		this.myMessageIndex = 1;
		this.lastClientInfoTransmitTime = 0;
		this.listeners = new LinkedList<IConversationListener>();
		this.config = config;
	}
	
	public void receiveMessage(ChatMessage chatMsg) {
		if(chatMsg.isContentMessage()) {
			addMessage(getPeer(), chatMsg.getMessage());
			notifyListeners();
		} else if(chatMsg.isTypingMessage()) {
			if(chatMsg.getTypingVal() == 1) {
				notifyListenersOfTyping();
			} else {
				notifyListenersTypingCeased();
			}
		}
		
		sendClientInfoIfNecessary();
	}

	public void sendMessage(String text) {
		if(!getPeer().isOnline()) {
			return;
		}
		
		ChatMessage message = new ChatMessage();
		message.setSessionId(getPeer().getSessionId());
		message.setContentPayload(myMessageIndex++, text);
		messageSender.sendMessage(message);
		
		sendClientInfoIfNecessary();

		addMessage(getSelf(), text);
		notifyListeners();
	}

	private void sendClientInfoIfNecessary() {
		if(System.currentTimeMillis() - lastClientInfoTransmitTime > ProtocolConstants.CLIENT_INFO_TRANSMIT_INTERVAL) {
			messageSender.sendMessage(generateClientInfo());
			lastClientInfoTransmitTime = System.currentTimeMillis();
		}
	}
	
	private ChatMessage generateClientInfo() {
		ChatMessage message = new ChatMessage();
		message.setSessionId(getPeer().getSessionId());
		InetSocketAddress netAddr = getSelf().getAddress();
		InetSocketAddress localAddr;
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			localAddr = new InetSocketAddress(localHost, config.getLocalPort());
		} catch (UnknownHostException e) {
			Logging.connectionLogger.warning("Unable to resolve local host address: will report 192.168.0.1 to peer");
			localAddr = InetSocketAddress.createUnresolved("192.168.0.1", config.getLocalPort());
		}
		
		message.setClientInfoPayload(netAddr, localAddr, 1L, IOUtil.generateSaltString());
		return message;
	}

	private void addMessage(Friend user, String message) {
		chatLog.add(new ConversationLogLine(user, message));
	}
	
	public String getChatLog(boolean withTimestamps) {
		StringBuffer entireChatLog = new StringBuffer();
		for(ConversationLogLine chatLine : chatLog) {
			entireChatLog.append(chatLine.toString(withTimestamps));
		}
		
		return entireChatLog.toString();
	}
	
	public ConversationLogLine getLastMessage() {
		return chatLog.getLast();
	}
	
	public void addChatListener(IConversationListener listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners() {
		for(IConversationListener listener : listeners) {
			listener.chatLogUpdated();
		}
	}
	
	public void notifyListenersOfTyping() {
		for(IConversationListener listener : listeners) {
			listener.peerIsTyping();
		}
	}
	
	public void notifyListenersTypingCeased() {
		for(IConversationListener listener : listeners) {
			listener.peerIsNotTyping();
		}
	}
	
	public Friend getPeer() {
		return friendsList.getFriend(peerUid);
	}

	public Friend getSelf() {
		return friendsList.getSelf();
	}

	public int getNumberMessages() {
		return chatLog.size();
	}

	public ConversationLogLine getChatLogLine(int index) {
		return chatLog.get(index);
	}
}
