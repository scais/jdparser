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

import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;

public interface IConversation {

	public abstract void receiveMessage(ChatMessage chatMsg);

	public abstract void sendMessage(String text);

	public abstract String getChatLog(boolean withTimestamps);

	public abstract ConversationLogLine getLastMessage();

	public abstract void addChatListener(IConversationListener listener);

	public abstract void notifyListeners();

	public abstract void notifyListenersOfTyping();

	public abstract Friend getPeer();

}