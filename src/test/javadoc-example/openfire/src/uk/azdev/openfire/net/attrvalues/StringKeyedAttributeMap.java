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

import static uk.azdev.openfire.net.util.IOUtil.*;

public class StringKeyedAttributeMap extends AttributeMap<String> {

	public static final int MAX_KEY_LENGTH = (1 << 8) - 1;
	private static final int ATTR_NAME_LEN_SIZE = 1;
	
	@Override
	protected String readKey(ByteBuffer buffer) {
		int attrNameLen = readUnsignedByte(buffer);

		byte[] attrNameBytes = new byte[attrNameLen];
		buffer.get(attrNameBytes);
		return decodeString(attrNameBytes);
	}
	
	@Override
	protected void writeKey(ByteBuffer buffer, String attrName) {
		buffer.put((byte)attrName.length());
		buffer.put(encodeString(attrName));
	}
	
	@Override
	protected int getKeySize(String key) {
		return ATTR_NAME_LEN_SIZE + key.length();
	}
	
	@Override
	protected boolean checkKey(String name) {
		return name != null && name.length() <= MAX_KEY_LENGTH;
	}

}
