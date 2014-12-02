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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class FriendStatusMessage extends StringMapBasedMessage {

	public static final int FRIEND_STATUS_MESSAGE_ID = 154;

	private static final String SESSION_ID_LIST_KEY = "sid";
	private static final String STATUS_LIST_KEY = "msg";

	private Map<SessionId, String> sessionIdToStatusMap;
	
	public FriendStatusMessage() {
		sessionIdToStatusMap = new LinkedHashMap<SessionId, String>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue sessionIdListValue = ((ListAttributeValue)map.getAttributeValue(SESSION_ID_LIST_KEY));
		List<String> statusList = map.getAttributeValueAsList(STATUS_LIST_KEY, new StringAttributeValue());
		
		Iterator<AttributeValue<?>> sessionIdIter = sessionIdListValue.getValue().iterator();
		Iterator<String> statusIter = statusList.iterator();
		while(sessionIdIter.hasNext()) {
			SessionIdAttributeValue sidValue = (SessionIdAttributeValue)sessionIdIter.next();
			String status = statusIter.next();
			sessionIdToStatusMap.put(sidValue.getValue(), status);
		}
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue sessionIdList = new ListAttributeValue();
		ListAttributeValue statusList = new ListAttributeValue();
		for(Entry<SessionId, String> entry : sessionIdToStatusMap.entrySet()) {
			sessionIdList.addValue(new SessionIdAttributeValue(entry.getKey()));
			statusList.addValue(new StringAttributeValue(entry.getValue()));
		}
		
		map.addAttribute(SESSION_ID_LIST_KEY, sessionIdList);
		map.addAttribute(STATUS_LIST_KEY, statusList);
	}

	public int getMessageId() {
		return FRIEND_STATUS_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new FriendStatusMessage();
	}

	public Set<SessionId> getSessionIdSet() {
		return sessionIdToStatusMap.keySet();
	}

	public String getStatusForSessionId(SessionId sessionId) {
		return sessionIdToStatusMap.get(sessionId);
	}

	public void addStatus(SessionId sessionId, String status) {
		sessionIdToStatusMap.put(sessionId, status);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Friend Status Message");
		buffer.append("\n\tStatuses:");
		
		for(Entry<SessionId, String> entry : sessionIdToStatusMap.entrySet()) {
			buffer.append("\n\t");
			buffer.append(entry.getKey().toString());
			buffer.append(" -> \"");
			buffer.append(entry.getValue());
			buffer.append("\"");
		}
		return buffer.toString();
	}

}
