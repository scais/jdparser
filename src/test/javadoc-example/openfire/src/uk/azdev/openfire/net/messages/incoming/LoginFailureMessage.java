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

import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class LoginFailureMessage extends StringMapBasedMessage {

	public static final int LOGIN_FAILURE_MESSAGE_ID = 129;

	private static final String REASON_KEY = "reason";
	
	private long reason;
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		reason = map.getInt32AttributeValue(REASON_KEY);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(REASON_KEY, reason);
	}

	public int getMessageId() {
		return LOGIN_FAILURE_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new LoginFailureMessage();
	}

	public long getReason() {
		return reason;
	}

	public void setReason(long reason) {
		this.reason = reason;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Login Failure Message");
		buffer.append("\n\tReason: ");
		buffer.append(reason);
		
		return buffer.toString();
	}
}
