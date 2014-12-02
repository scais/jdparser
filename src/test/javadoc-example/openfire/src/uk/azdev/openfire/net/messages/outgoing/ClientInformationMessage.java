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

import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class ClientInformationMessage extends StringMapBasedMessage {

	public static final int CLIENT_INFO_MESSAGE_ID = 18;

	private String[] skinList = new String[4];
	private Long[] version = new Long[4];
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		skinList = map.getAttributeValueAsList("skin", new StringAttributeValue()).toArray(new String[0]);
		version = map.getAttributeValueAsList("version", new Int32AttributeValue()).toArray(new Long[0]);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		populateSkinList(map);
		populateVersionList(map);
	}

	private void populateVersionList(StringKeyedAttributeMap map) {
		ListAttributeValue versionListValue = new ListAttributeValue();
		for(Long versionSegment : version) {
			versionListValue.addValue(new Int32AttributeValue(versionSegment));
		}
		map.addAttribute("version", versionListValue);
	}

	private void populateSkinList(StringKeyedAttributeMap map) {
		ListAttributeValue skinListValue = new ListAttributeValue();
		for(String skinSegment : skinList) {
			skinListValue.addValue(new StringAttributeValue(skinSegment));
		}
		map.addAttribute("skin", skinListValue);
	}

	public String getVersion() {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i < version.length - 1; i++) {
			buffer.append(version[i]);
			buffer.append('.');
		}
		
		buffer.append(version[version.length-1]);
		return buffer.toString();
	}
	
	public void setVersion(String versionStr) {
		String[] parts = versionStr.split("\\.");
		version = new Long[parts.length];
		
		for(int i=0; i < parts.length; i++) {
			version[i] = Long.parseLong(parts[i]);
		}
	}
	
	public String[] getSkinList() {
		return skinList;
	}
	
	public void setSkinList(String[] skins) {
		this.skinList = skins;
	}
	
	public int getMessageId() {
		return CLIENT_INFO_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new ClientInformationMessage();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Client information message");
		buffer.append("\n\tSkin list: ");
		for(String skin : skinList) {
			buffer.append(skin);
			buffer.append(" ");
		}
		buffer.append("\n\tVersion: ");
		buffer.append(getVersion());
		return buffer.toString();
	}

}
