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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.net.util.UnrolledListAttributeValue;

import static uk.azdev.openfire.net.util.IOUtil.*;

/**
 * Represents a variable length list of uniformly typed items.
 */
public class ListAttributeValue implements AttributeValue<List<AttributeValue<?>>> {

	private static final int ITEM_TYPE_SIZE = 1;
	private static final int NUM_ITEMS_SIZE = 2;
	
	public static final int TYPE_ID = 4;
	
	public static final int MAX_ITEM_TYPE_ID = (1 << 8) - 1;
	public static final int MAX_LIST_SIZE = (1 << 16) - 1;
	
	private List<AttributeValue<?>> valueList;
	private int itemType;
	
	public ListAttributeValue() {
		valueList = new LinkedList<AttributeValue<?>>();
		itemType = StringAttributeValue.TYPE_ID;
	}
	
	public List<AttributeValue<?>> getValue() {
		return valueList;
	}
	
	public <T> List<T> getListContents(AttributeValue<T> expectedAttrType) {
		if(getItemType() != expectedAttrType.getTypeId()) {
			throw new IllegalArgumentException("list is not of type \"" + expectedAttrType.getTypeId() + "\"");
		}
		
		return new UnrolledListAttributeValue<T>(this);
	}
	
	/**
	 * Adds an item to the list. If this is the first item to be added
	 * to the list, then the list's item type is set to the type of the
	 * added item.
	 * @throws MaxListSizeExceededException if the list is full
	 * @throws IllegalArgumentException if an attempt is made to add
	 * an item to a non-empty list which has a different type id (i.e.
	 * adding an integer value to a list of strings).
	 */
	public void addValue(AttributeValue<?> value) {
		checkAddValuePreconditions(value);
		
		itemType = value.getTypeId();
		valueList.add(value);
	}

	private void checkAddValuePreconditions(AttributeValue<?> value) {
		if(valueList.size() == MAX_LIST_SIZE) {
			throw new MaxListSizeExceededException();
		}
		if(valueList.size() > 0 && itemType != value.getTypeId()) {
			throw new IllegalArgumentException("cannot have mixed types in a list");
		}
	}
	
	public int getSize() {
		int size = ITEM_TYPE_SIZE + NUM_ITEMS_SIZE;
		
		for(AttributeValue<?> attrVal : valueList) {
			size += attrVal.getSize();
		}
		
		return size;
	}

	public int getTypeId() {
		return TYPE_ID;
	}
	
	public int getItemType() {
		return itemType;
	}

	public void readValue(ByteBuffer buffer) {
		itemType = readUnsignedByte(buffer);
		int numItems = readUnsignedShort(buffer);
		
		AttributeValueFactory valueFactory = getValueFactory();
		
		for(int i=0; i < numItems; i++) {
			AttributeValue<?> value = valueFactory.createAttributeValue(itemType);
			value.readValue(buffer);
			valueList.add(value);
		}
	}
	
	protected AttributeValueFactory getValueFactory() {
		return new AttributeValueFactory();
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.put((byte)itemType);
		buffer.putShort((short)valueList.size());
		
		for(AttributeValue<?> value : valueList) {
			value.writeValue(buffer);
		}
	}

	public ListAttributeValue newInstance() {
		return new ListAttributeValue();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ ");
		
		Iterator<AttributeValue<?>> iter = valueList.iterator();
		while(iter.hasNext()) {
			AttributeValue<?> value = iter.next();
			buffer.append(value);
			if(iter.hasNext()) {
				buffer.append(", ");
			}
		}
		
		buffer.append(" ]");
		
		return buffer.toString();
	}
}
