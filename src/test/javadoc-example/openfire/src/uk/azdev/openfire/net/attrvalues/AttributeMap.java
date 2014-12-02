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

import static uk.azdev.openfire.net.util.IOUtil.readUnsignedByte;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AttributeMap<K> {

	public static final int MAX_ATTRIBUTES = (1 << 8) - 1;
	private static final int NUM_ATTRS_SIZE = 1;
	private static final int ATTR_TYPE_SIZE = 1;
	
	private Map<K, AttributeValue<?>> attributeMap;
	
	public AttributeMap() {
		// we care about the order of the items in the map,
		// to make them match precisely the order xfire produces
		attributeMap = new LinkedHashMap<K, AttributeValue<?>>();
	}
	
	public int numAttributes() {
		return attributeMap.size();
	}
	
	public boolean hasAttribute(K key) {
		return attributeMap.containsKey(key);
	}
	
	public AttributeValue<?> getAttributeValue(K key) {
		if(!hasAttribute(key)) {
			throw new IllegalArgumentException("no attribute with key <" + key + "> found");
		}
		return attributeMap.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttributeValue(K key, AttributeValue<T> expectedType) {
		AttributeValue<?> value = getAttributeValue(key);
		if(value.getTypeId() != expectedType.getTypeId()) {
			throw new IllegalArgumentException("Attribute value for key \"" + key + "\" does not map to a value of type \"" + expectedType.getTypeId() + "\"");
		}
		
		return (T)value.getValue();
	}
	
	/**
	 * Convenience method for fetching string values.
	 */
	public String getStringAttributeValue(K key) {
		return getAttributeValue(key, new StringAttributeValue());
	}
	
	private ListAttributeValue getListAttributeValue(K key) {
		AttributeValue<?> value = getAttributeValue(key);
		if(!(value instanceof ListAttributeValue)) {
			throw new IllegalArgumentException("key <" + key + "> does not map to a list value");
		}
		
		return (ListAttributeValue) value;
	}

	public <T> List<T> getAttributeValueAsList(K key, AttributeValue<T> expectedAttrType) {
		try {
			return getListAttributeValue(key).getListContents(expectedAttrType);
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("key <" + key + "> does not map to a list with item type \"" + expectedAttrType.getTypeId() + "\"");
		}
	}
	
	/**
	 * Convenience method for fetching int32 values
	 */
	public long getInt32AttributeValue(K key) {
		AttributeValue<?> value = getAttributeValue(key);
		if(!(value instanceof Int32AttributeValue)) {
			throw new IllegalArgumentException("key <" + key + "> does not map to an int32 value");
		}
		
		return ((Int32AttributeValue)value).getValue();
	}
	
	public void addAttribute(K key, AttributeValue<?> value) {
		checkAddAttributePreconditions(key, value);
		attributeMap.put(key, value);
	}
	
	/**
	 * Convenience method to add an attribute with a string value.
	 */
	public void addAttribute(K key, String value) {
		addAttribute(key, new StringAttributeValue(value));
	}
	
	/**
	 * Convenience method to add an attribute with a 32-bit unsigned int value.
	 */
	public void addAttribute(K key, long value) {
		addAttribute(key, new Int32AttributeValue(value));
	}
	
	private void checkAddAttributePreconditions(K key, AttributeValue<?> value) {
		if(attributeMap.size() == MAX_ATTRIBUTES) {
			throw new TooManyAttributesException("maximum number of attributes exceeded");
		}
		
		if(attributeMap.containsKey(key)) {
			throw new DuplicateAttributeException("attempt to add attribute \"" + key + "\" twice");
		}
		
		if(!checkKey(key)) {
			throw new IllegalArgumentException("invalid attribute key");
		}
		
		if(value == null) {
			throw new IllegalArgumentException("null attribute value");
		}
	}

	public void read(ByteBuffer buffer) {
		int numAttrs = readUnsignedByte(buffer);
		
		for(int i=0; i < numAttrs; i++) {
			K key = readKey(buffer);
			AttributeValue<?> value = readAttributeValue(buffer);
			addAttribute(key, value);
		}
	}
	
	private AttributeValue<?> readAttributeValue(ByteBuffer buffer) {
		int attrType = buffer.get() & 0xFF;
		
		AttributeValueFactory factory = new AttributeValueFactory();
		AttributeValue<?> value = factory.createAttributeValue(attrType);
		value.readValue(buffer);
		return value;
	}
	
	public void write(ByteBuffer buffer) {
		buffer.put((byte)numAttributes());
		
		for(Entry<K, AttributeValue<?>> entry : attributeMap.entrySet()) {
			writeKey(buffer, entry.getKey());
			writeAttributeValue(buffer, entry.getValue());
		}
	}
	
	private void writeAttributeValue(ByteBuffer buffer, AttributeValue<?> value) {
		buffer.put((byte)value.getTypeId());
		value.writeValue(buffer);
	}

	public int getSize() {
		int size = NUM_ATTRS_SIZE;
		for(Entry<K, AttributeValue<?>> entry : attributeMap.entrySet()) {
			size += getKeySize(entry.getKey());
			size += ATTR_TYPE_SIZE + entry.getValue().getSize();
		}
		
		return size;
	}
	
	protected abstract int getKeySize(K key);
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(Entry<K, AttributeValue<?>> entry : attributeMap.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(" = ");
			buffer.append(entry.getValue());
			buffer.append('\n');
		}
		
		return buffer.toString();
	}
	
	/**
	 * Implementors should use this to check that the key value
	 * is acceptable. If not, the method should return false.
	 */
	protected abstract boolean checkKey(K key);
	
	protected abstract void writeKey(ByteBuffer buffer, K key);
	protected abstract K readKey(ByteBuffer buffer);
}
