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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class FriendListMessage extends StringMapBasedMessage {

	public static final int FRIEND_LIST_MESSAGE_ID = 131;
	private static final String USER_ID_LIST_KEY = "userid";
	private static final String USER_NAME_LIST_KEY = "friends";
	private static final String USER_NICK_LIST_KEY = "nick";
	
	private List<Long> userIds;
	private Map<Long, String> userIdToNameMap;
	private Map<Long, String> userIdToNickMap;

	public FriendListMessage() {
		userIds = new ArrayList<Long>();
		userIdToNameMap = new LinkedHashMap<Long, String>();
		userIdToNickMap = new LinkedHashMap<Long, String>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		userIds = map.getAttributeValueAsList(USER_ID_LIST_KEY, new Int32AttributeValue());
		
		List<String> userNames = map.getAttributeValueAsList(USER_NAME_LIST_KEY, new StringAttributeValue());
		List<String> userNicks = map.getAttributeValueAsList(USER_NICK_LIST_KEY, new StringAttributeValue());
		
		Iterator<String> userNamesIter = userNames.iterator();
		Iterator<String> userNicksIter = userNicks.iterator();
		for(Long userId : userIds) {
			userIdToNameMap.put(userId, userNamesIter.next());
			userIdToNickMap.put(userId, userNicksIter.next());
		}
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue userIdsValue = new ListAttributeValue();
		ListAttributeValue userNamesValue = new ListAttributeValue();
		ListAttributeValue userNicksValue = new ListAttributeValue();
		for(Long id : userIds) {
			userIdsValue.addValue(new Int32AttributeValue(id));
			userNamesValue.addValue(new StringAttributeValue(userIdToNameMap.get(id)));
			userNicksValue.addValue(new StringAttributeValue(userIdToNickMap.get(id)));
		}
		
		map.addAttribute(USER_NAME_LIST_KEY, userNamesValue);
		map.addAttribute(USER_NICK_LIST_KEY, userNicksValue);
		map.addAttribute(USER_ID_LIST_KEY, userIdsValue);
	}

	public int getMessageId() {
		return FRIEND_LIST_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new FriendListMessage();
	}

	public List<Long> getUserIdList() {
		return userIds;
	}

	public String getUsernameForId(long userId) {
		return userIdToNameMap.get(userId);
	}

	public String getUserNickForId(long userId) {
		return userIdToNickMap.get(userId);
	}

	public void addUser(long userId, String userName, String userNick) {
		userIds.add(userId);
		userIdToNameMap.put(userId, userName);
		userIdToNickMap.put(userId, userNick);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Friend List Message");
		buffer.append("\n\tUsers:");
		
		for(long userId : userIds) {
			buffer.append("\n\t");
			buffer.append(userId);
			buffer.append(" -> ");
			buffer.append(userIdToNameMap.get(userId));
			buffer.append(" \"");
			buffer.append(userIdToNickMap.get(userId));
			buffer.append('\"');
		}
		
		return buffer.toString();
	}

}
