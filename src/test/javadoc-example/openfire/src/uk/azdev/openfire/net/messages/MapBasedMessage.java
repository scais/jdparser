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

import java.nio.ByteBuffer;

import uk.azdev.openfire.net.attrvalues.AttributeMap;


public abstract class MapBasedMessage<MapKeyType, MapType extends AttributeMap<MapKeyType>> implements IMessage {

	protected MapType attrMap;
	
	public int getMessageContentSize() {
		MapType populatedMap = getPopulatedAttributeMap();
		return populatedMap.getSize();
	}
	
	private MapType getPopulatedAttributeMap() {
		if(attrMap == null) {
			attrMap = createAttributeMap();
			populateAttributeMap(attrMap);
		}
		
		return attrMap;
	}
	
	public void readMessageContent(ByteBuffer buffer) {
		attrMap = createAttributeMap();
		attrMap.read(buffer);
		interpretAttributeMap(attrMap);
	}
	
	protected abstract void interpretAttributeMap(MapType map);

	public void writeMessageContent(ByteBuffer buffer) {
		MapType populatedMap = getPopulatedAttributeMap();
		populatedMap.write(buffer);
	}
	
	protected abstract MapType createAttributeMap();
	protected abstract void populateAttributeMap(MapType map);

}
