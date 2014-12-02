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
package uk.azdev.openfire.common;

import uk.azdev.openfire.net.util.IOUtil;

public abstract class ByteArrayBasedId {
	
	private byte[] value;
	
	public ByteArrayBasedId() {
		value = new byte[getArraySize()];
	}
	
	public abstract int getArraySize();
	
	public ByteArrayBasedId(byte[] bytes) {
		this();
		if(bytes.length != getArraySize()) {
			throw new IllegalArgumentException("byte[] provided was not of correct size");
		}
		
		System.arraycopy(bytes, 0, value, 0, bytes.length);
	}
	
	public ByteArrayBasedId(int idAsInt) {
		this();
		value[15] = (byte)(idAsInt & 0xFF);
		value[14] = (byte)((idAsInt >> 8) & 0xFF);
		value[13] = (byte)((idAsInt >> 16) & 0xFF);
		value[12] = (byte)(idAsInt >> 24);
	}

	public byte[] getBytes() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ByteArrayBasedId)) {
			return false;
		}
		
		ByteArrayBasedId babId = (ByteArrayBasedId)o;
		for(int i=0; i < getArraySize(); i++) {
			if(value[i] != babId.value[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		for(int i=0; i < (getArraySize() / 4); i++) {
			hash ^= getInt(value, i*4);
		}
		
		return hash;
	}
	
	private int getInt(byte[] bytes, int offset) {
		return  bytes[offset] << 24
		      | bytes[offset+1] << 16
		      | bytes[offset+2] << 8
		      | bytes[offset+3];
	}
	
	@Override
	public String toString() {
		return IOUtil.printByteArray(value);
	}

	public boolean isZero() {
		for(int i=0; i < value.length; i++) {
			if(value[i] != 0) {
				return false;
			}
		}
		
		return true;
	}
}
