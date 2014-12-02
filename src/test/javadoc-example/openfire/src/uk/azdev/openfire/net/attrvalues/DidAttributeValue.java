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

import uk.azdev.openfire.common.DId;

/**
 * The "did" attribute value, which comes packaged as part of
 * message type 400. It's purpose is, at present, unknown.
 */
public class DidAttributeValue implements AttributeValue<DId> {

	public static final int TYPE_ID = 6;
	private static final int DID_SIZE = 21;
	
	private DId value;
	
	public DidAttributeValue() {
		value = new DId();
	}
	
	public DidAttributeValue(DId did) {
		value = did;
	}

	public DId getValue() {
		return value;
	}
	
	public int getSize() {
		return DID_SIZE;
	}

	public int getTypeId() {
		return TYPE_ID;
	}

	public DidAttributeValue newInstance() {
		return new DidAttributeValue();
	}

	public void readValue(ByteBuffer buffer) {
		byte[] bytes = new byte[DId.DID_SIZE];
		buffer.get(bytes);
		value = new DId(bytes);
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.put(value.getBytes());
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
