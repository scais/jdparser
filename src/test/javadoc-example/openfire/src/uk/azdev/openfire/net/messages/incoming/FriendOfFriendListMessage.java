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
package uk.azdev.openfire.net.messages.incoming;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.attrvalues.AttributeValue;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class FriendOfFriendListMessage extends StringMapBasedMessage {

	private static final String SID_LIST_KEY = "fnsid";
	private static final String UID_LIST_KEY = "userid";
	private static final String UNAME_LIST_KEY = "name";
	private static final String NICK_LIST_KEY = "nick";
	private static final String FRIENDS_LIST_KEY = "friends";

	public static final int FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE = 136;
	
	// friends of friends
	private List<Friend> friendsOfFriends;
	
	// friends of friends of friends
	private Map<Long, List<Long>> friendsInCommon;
	
	public FriendOfFriendListMessage() {
		friendsOfFriends = new LinkedList<Friend>();
		friendsInCommon = new HashMap<Long, List<Long>>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		List<Long> userIds = map.getAttributeValueAsList(UID_LIST_KEY, new Int32AttributeValue());
		List<SessionId> sessionIds = map.getAttributeValueAsList(SID_LIST_KEY, new SessionIdAttributeValue());
		List<String> userNames = map.getAttributeValueAsList(UNAME_LIST_KEY, new StringAttributeValue());
		List<String> displayNames = map.getAttributeValueAsList(NICK_LIST_KEY, new StringAttributeValue());
		List<List<AttributeValue<?>>> thirdDegree = map.getAttributeValueAsList(FRIENDS_LIST_KEY, new ListAttributeValue());
		
		Iterator<Long> userIdsIter = userIds.iterator();
		Iterator<SessionId> sessionIdsIter = sessionIds.iterator();
		Iterator<String> userNamesIter = userNames.iterator();
		Iterator<String> displayNamesIter = displayNames.iterator();
		Iterator<List<AttributeValue<?>>> friendsListIter = thirdDegree.iterator();
		
		while(userIdsIter.hasNext()) {
			long userId = userIdsIter.next();
			SessionId sessionId = sessionIdsIter.next();
			String userName = userNamesIter.next();
			String displayName = displayNamesIter.next();
			
			Friend friend = new Friend(userId, userName, displayName, sessionId);
			friendsOfFriends.add(friend);
			
			List<Long> otherFriends = extractThirdDegree(friendsListIter.next());
			friendsInCommon.put(userId, otherFriends);
		}
	}

	private List<Long> extractThirdDegree(List<AttributeValue<?>> otherFriends) {
		
		List<Long> otherFriendsTranslated = new LinkedList<Long>();
		
		for(AttributeValue<?> otherFriend : otherFriends) {
			if(!(otherFriend instanceof Int32AttributeValue)) {
				throw new IllegalArgumentException("Non-int32 found in third degree list!");
			}
			
			Int32AttributeValue otherFriendId = (Int32AttributeValue) otherFriend;
			otherFriendsTranslated.add(otherFriendId.getValue());
		}
		
		return otherFriendsTranslated;
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		
		ListAttributeValue uidList = new ListAttributeValue();
		ListAttributeValue sidList = new ListAttributeValue();
		ListAttributeValue nameList = new ListAttributeValue();
		ListAttributeValue nickList = new ListAttributeValue();
		ListAttributeValue connectionsList = new ListAttributeValue();
		
		for(Friend f : friendsOfFriends) {
			uidList.addValue(new Int32AttributeValue(f.getUserId()));
			sidList.addValue(new SessionIdAttributeValue(f.getSessionId()));
			nameList.addValue(new StringAttributeValue(f.getUserName()));
			nickList.addValue(new StringAttributeValue(f.getDisplayName()));
			
			ListAttributeValue friendList = new ListAttributeValue();
			for(Long friendId : getFriendsInCommon(f.getUserId())) {
				friendList.addValue(new Int32AttributeValue(friendId));
			}
			
			connectionsList.addValue(friendList);
		}
		
		map.addAttribute(SID_LIST_KEY, sidList);
		map.addAttribute(UID_LIST_KEY, uidList);
		map.addAttribute(UNAME_LIST_KEY, nameList);
		map.addAttribute(NICK_LIST_KEY, nickList);
		map.addAttribute(FRIENDS_LIST_KEY, connectionsList);
	}

	public int getMessageId() {
		return FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE;
	}

	public IMessage newInstance() {
		return new FriendOfFriendListMessage();
	}

	public List<Friend> getFriendsOfFriends() {
		return friendsOfFriends;
	}
	
	public List<Long> getFriendsInCommon(long userId) {
		return friendsInCommon.get(userId);
	}

	public void addFriend(Friend friend, List<Long> friendConnections) {
		friendsOfFriends.add(friend);
		friendsInCommon.put(friend.getUserId(), friendConnections);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Friend of Friend List Message\n");
		
		for(Friend friend : friendsOfFriends) {
			
			buffer.append("\t");
			buffer.append(friend.toString());
			buffer.append(" -> ");
			
			List<Long> friends = friendsInCommon.get(friend.getUserId());
			Iterator<Long> friendsIter = friends.iterator();
			while(friendsIter.hasNext()) {
				buffer.append(friendsIter.next());
				
				if(friendsIter.hasNext()) {
					buffer.append(", ");
				}
			}
			
			buffer.append("\n");
		}
		
		return buffer.toString();
	}
}
