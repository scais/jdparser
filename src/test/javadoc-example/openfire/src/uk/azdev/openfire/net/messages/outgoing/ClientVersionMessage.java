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
import uk.azdev.openfire.net.util.BoundsUtil;

public class ClientVersionMessage extends StringMapBasedMessage {

	private static final String VERSION_KEY = "version";

	public static final int CLIENT_VERSION_MESSAGE_ID = 3;

	private long version;
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		version = map.getInt32AttributeValue(VERSION_KEY);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(VERSION_KEY, version);
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		BoundsUtil.checkInt32Bounds(version, "version");
		this.version = version;
	}

	public int getMessageId() {
		return CLIENT_VERSION_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new ClientVersionMessage();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Client Version Message\n");
		buffer.append("\tVersion: ");
		buffer.append(version);
		return buffer.toString();
	}

}
