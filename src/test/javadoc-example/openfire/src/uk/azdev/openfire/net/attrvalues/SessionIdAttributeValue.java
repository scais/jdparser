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
package uk.azdev.openfire.net.attrvalues;

import java.nio.ByteBuffer;

import uk.azdev.openfire.common.SessionId;

public class SessionIdAttributeValue implements AttributeValue<SessionId> {

	public static final int TYPE_ID = 3;

	private SessionId sessionId;
	
	public SessionIdAttributeValue() {
		sessionId = new SessionId();
	}
	
	public SessionIdAttributeValue(SessionId sessionId) {
		this.sessionId = sessionId;
	}
	
	public SessionId getValue() {
		return sessionId;
	}
	
	public void setSessionId(SessionId sessionId) {
		this.sessionId = sessionId;
	}
	
	public int getSize() {
		return SessionId.SESSION_ID_SIZE;
	}

	public int getTypeId() {
		return TYPE_ID;
	}

	public SessionIdAttributeValue newInstance() {
		return new SessionIdAttributeValue();
	}

	public void readValue(ByteBuffer buffer) {
		byte[] sessionIdBytes = new byte[SessionId.SESSION_ID_SIZE];
		buffer.get(sessionIdBytes);
		sessionId = new SessionId(sessionIdBytes);
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.put(sessionId.getBytes());
	}

	@Override
	public String toString() {
		return sessionId.toString();
	}
	
}
