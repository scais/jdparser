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
package uk.azdev.openfire.friendlist.messageprocessors;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.conversations.IConversation;
import uk.azdev.openfire.conversations.IConversationStore;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;

public class ChatMessageProcessor implements IMessageProcessor {

	private IConversationStore conversationStore;
	private ConnectionEventListener listener;
	private IMessageSender messageSender;
	
	public ChatMessageProcessor(IConversationStore conversationStore, ConnectionEventListener listener, IMessageSender messageSender) {
		this.conversationStore = conversationStore;
		this.listener = listener;
		this.messageSender = messageSender;
	}
	
	public void processMessage(IMessage msg) {
		ChatMessage message = (ChatMessage)msg;
		IConversation conversation = conversationStore.getConversation(message.getSessionId());
		conversation.receiveMessage(message);
		
		if(message.isContentMessage()) {
			sendAck(message);
			listener.conversationUpdate(message.getSessionId());
		}
	}

	private void sendAck(ChatMessage message) {
		ChatMessage ack = new ChatMessage();
		ack.setSessionId(message.getSessionId());
		ack.setAcknowledgementPayload(message.getMessageIndex());
		messageSender.sendMessage(ack);
	}

}
