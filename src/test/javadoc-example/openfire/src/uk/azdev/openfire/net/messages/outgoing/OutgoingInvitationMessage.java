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

import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class OutgoingInvitationMessage extends StringMapBasedMessage {

	public static final int TYPE_ID = 6;
	
	private static final String USER_NAME_KEY = "name";
	private static final String MESSAGE_KEY = "msg";
	
	private String userName;
	private String message;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		userName = map.getStringAttributeValue(USER_NAME_KEY);
		message = map.getStringAttributeValue(MESSAGE_KEY);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(USER_NAME_KEY, userName);
		map.addAttribute(MESSAGE_KEY, message);
	}

	public int getMessageId() {
		return TYPE_ID;
	}

	public IMessage newInstance() {
		return new OutgoingInvitationMessage();
	}

}
