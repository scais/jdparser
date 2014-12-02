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

import java.util.List;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.FriendListMessage;

public class FriendListMessageProcessor implements IMessageProcessor {
	
	private ConnectionEventListener listener;
	private FriendsList friendsList;
	
	public FriendListMessageProcessor(FriendsList friendsList, ConnectionEventListener listener) {
		this.friendsList = friendsList;
		this.listener = listener;
	}

	public void processMessage(IMessage msg) {
		FriendListMessage message = (FriendListMessage)msg;
		List<Long> userIds = message.getUserIdList();
		
		friendsList.acquireLock();
		try {
			for(long userId : userIds) {
				if(!friendsList.containsFriend(userId)) {
					Friend friend = new Friend(userId, message.getUsernameForId(userId), message.getUserNickForId(userId));
					friendsList.addFriend(friend, friendsList.getSelf());
				}
			}
			listener.friendsListUpdated();
		} finally {
			friendsList.releaseLock();
		}
		
		
		
	}
	
}
