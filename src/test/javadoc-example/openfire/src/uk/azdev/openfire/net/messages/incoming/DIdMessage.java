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

import uk.azdev.openfire.common.DId;
import uk.azdev.openfire.net.attrvalues.DidAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class DIdMessage extends StringMapBasedMessage {

	public static final int DID_MESSAGE_TYPE_ID = 400;
	
	private DId did;
	
	public DIdMessage() {
		did = new DId();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		did = map.getAttributeValue("did", new DidAttributeValue());
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute("did", new DidAttributeValue(did));
	}

	public int getMessageId() {
		return DID_MESSAGE_TYPE_ID;
	}

	public IMessage newInstance() {
		return new DIdMessage();
	}

	public DId getDId() {
		return did;
	}

	public void setDId(DId did) {
		this.did = did;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DID message\n\t");
		buffer.append("DID:<");
		buffer.append(did.toString());
		buffer.append(">");
		
		return buffer.toString();
	}

}
