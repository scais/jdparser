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
package uk.azdev.openfire.net.messages;

import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;

public class UnknownStringMapBasedMessage extends StringMapBasedMessage {

	private int id;

	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		// no interpretation needed
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		// no population needed
	}

	public int getMessageId() {
		return id;
	}
	
	public void setMessageId(int id) {
		this.id = id;
	}

	public IMessage newInstance() {
		return new UnknownStringMapBasedMessage();
	}
	
	@Override
	public String toString() {
		return "Message type: " + getMessageId() + "\n" + attrMap.toString();
	}

}
