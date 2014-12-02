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
package uk.azdev.openfire.net.messages.outgoing;

import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class OnlineFriendsRequest extends StringMapBasedMessage {

	public static final int TYPE_ID = 5; 
	
	private static final String SID_LIST_KEY = "sid";
	
	private List<SessionId> sessionIds = new LinkedList<SessionId>();
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		sessionIds = map.getAttributeValueAsList(SID_LIST_KEY, new SessionIdAttributeValue());
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue sidList = new ListAttributeValue();
		for(SessionId sessionId : sessionIds) {
			sidList.addValue(new SessionIdAttributeValue(sessionId));
		}
		
		map.addAttribute(SID_LIST_KEY, sidList);
	}

	public int getMessageId() {
		return TYPE_ID;
	}

	public IMessage newInstance() {
		return new OnlineFriendsRequest();
	}

	public List<SessionId> getSessionIds() {
		return sessionIds;
	}

	public void addSessionId(SessionId sid) {
		sessionIds.add(sid);
	}

}
