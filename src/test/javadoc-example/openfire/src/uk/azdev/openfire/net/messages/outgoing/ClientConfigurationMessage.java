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

import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class ClientConfigurationMessage extends StringMapBasedMessage {

	public static final int CLIENT_CONFIGURATION_MESSAGE_ID = 16;

	private static final String LANGUAGE_KEY = "lang";
	private static final String ACTIVE_SKIN_KEY = "skin";
	private static final String ACTIVE_THEME_KEY = "theme";
	private static final String PARTNER_KEY = "partner";

	private String language;
	private String activeSkin;
	private String activeTheme;
	private String partner;
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		language = map.getStringAttributeValue(LANGUAGE_KEY);
		activeSkin = map.getStringAttributeValue(ACTIVE_SKIN_KEY);
		activeTheme = map.getStringAttributeValue(ACTIVE_THEME_KEY);
		partner = map.getStringAttributeValue(PARTNER_KEY);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(LANGUAGE_KEY, language);
		map.addAttribute(ACTIVE_SKIN_KEY, activeSkin);
		map.addAttribute(ACTIVE_THEME_KEY, activeTheme);
		map.addAttribute(PARTNER_KEY, partner);
	}

	public int getMessageId() {
		return CLIENT_CONFIGURATION_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new ClientConfigurationMessage();
	}

	public String getActiveSkin() {
		return activeSkin;
	}

	public void setActiveSkin(String activeSkin) {
		this.activeSkin = activeSkin;
	}

	public String getActiveTheme() {
		return activeTheme;
	}

	public void setActiveTheme(String activeTheme) {
		this.activeTheme = activeTheme;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Client configuration message");
		buffer.append("\n\tLanguage: ");
		buffer.append(language);
		buffer.append("\n\tActive skin: ");
		buffer.append(activeSkin);
		buffer.append("\n\tActive theme: ");
		buffer.append(activeTheme);
		buffer.append("\n\tPartner: ");
		buffer.append(partner);
		
		return buffer.toString();
	}
	
}
