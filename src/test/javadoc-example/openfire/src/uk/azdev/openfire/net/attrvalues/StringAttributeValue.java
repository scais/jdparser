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

public class StringAttributeValue implements AttributeValue<String> {

	public static final int TYPE_ID = 1;
	
	// the number of bytes used to indicate how long the string is
	private static final int STRING_LEN_SIZE = 2;
	public static final int MAX_STRING_SIZE = (1 << 16) - 1;
	
	private String value;
	
	public StringAttributeValue() {
		this("");
	}
	
	public StringAttributeValue(String value) {
		setValue(value);
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the string value.
	 * @throws IllegalArgumentException if the string is larger than
	 * <code>MAX_STRING_SIZE</code>
	 */
	public void setValue(String value) {
		if(value.length() > MAX_STRING_SIZE) {
			throw new IllegalArgumentException("String larger than maximum allowed!");
		}
		
		this.value = value;
	}
	
	public int getSize() {
		return STRING_LEN_SIZE + getEncodedStringSize(value);
	}

	public int getTypeId() {
		return TYPE_ID;
	}

	public void readValue(ByteBuffer buffer) {
		int size = readUnsignedShort(buffer);
		byte[] stringBytes = new byte[size];
		buffer.get(stringBytes);
		setValue(decodeString(stringBytes));
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.putShort((short)value.length());
		buffer.put(encodeString(value));
	}

	public StringAttributeValue newInstance() {
		return new StringAttributeValue();
	}

	@Override
	public String toString() {
		return '\"' + value + '\"';
	}
}
