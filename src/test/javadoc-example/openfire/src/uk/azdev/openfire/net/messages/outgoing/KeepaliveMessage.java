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

import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;
import uk.azdev.openfire.net.util.BoundsUtil;

public class KeepaliveMessage extends StringMapBasedMessage {

	public static final int KEEP_ALIVE_MESSAGE_ID = 13;
	
	private static final String VALUE_KEY = "value";
	private static final String STATS_KEY = "stats";
	
	private long value;
	private List<Long> statsList;

	public KeepaliveMessage() {
		statsList = new LinkedList<Long>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		value = map.getInt32AttributeValue(VALUE_KEY);
		statsList = map.getAttributeValueAsList(STATS_KEY, new Int32AttributeValue());
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(VALUE_KEY, value);
		
		ListAttributeValue statsListValue = new ListAttributeValue();
		for(Long stat : statsList) {
			statsListValue.addValue(new Int32AttributeValue(stat));
		}
		
		map.addAttribute(STATS_KEY, statsListValue);
	}

	public int getMessageId() {
		return KEEP_ALIVE_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new KeepaliveMessage();
	}

	public long getValue() {
		return value;
	}
	
	public void setValue(long value) {
		BoundsUtil.checkInt32Bounds(value, "value");
		this.value = value;
	}
	
	public List<Long> getStatsList() {
		return statsList;
	}

	public void setStatsList(List<Long> statsList) {
		if(statsList == null) {
			throw new IllegalArgumentException("stats list cannot be null");
		}
		this.statsList = statsList;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Keepalive Message");
		buffer.append("\n\tValue: ");
		buffer.append(value);
		buffer.append("\n\tStats:");
		
		for(Long stat : statsList) {
			buffer.append("\n\t\t");
			buffer.append(stat);
		}
		
		return buffer.toString();
	}

}
