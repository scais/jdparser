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
import uk.azdev.openfire.net.util.BoundsUtil;
import uk.azdev.openfire.net.util.CryptoUtil;

public class LoginRequestMessage extends StringMapBasedMessage {
	
	public static final int LOGIN_REQUEST_MESSAGE_ID = 1;

	private static final String USERNAME_KEY = "name";
	private static final String PASSWORD_KEY = "password";
	private static final String FLAGS_KEY = "flags";
	
	private long flags;
	private String username;
	private String password;
	private String saltedPassword;
	private String salt;
	
	public int getMessageId() {
		return LOGIN_REQUEST_MESSAGE_ID;
	}
	
	public IMessage newInstance() {
		return new LoginRequestMessage();
	}
	
	public long getFlags() {
		return flags;
	}
	public void setFlags(long flags) {
		BoundsUtil.checkInt32Bounds(flags, "flags");
		this.flags = flags;
	}
	
	public String getSaltedPassword() {
		if(saltedPassword == null) {
			saltedPassword = CryptoUtil.getHashedPassword(username, password, salt); 
		}
		
		return saltedPassword;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		username = map.getStringAttributeValue(USERNAME_KEY);
		saltedPassword = map.getStringAttributeValue(PASSWORD_KEY);
		flags = map.getInt32AttributeValue(FLAGS_KEY);
	}
	
	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(USERNAME_KEY, username);
		map.addAttribute(PASSWORD_KEY, getSaltedPassword());
		map.addAttribute(FLAGS_KEY, flags);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Login request message");
		buffer.append("\n\tUser name: ");
		buffer.append(getUsername());
		buffer.append("\n\tSalted password: ");
		buffer.append(getSaltedPassword());
		buffer.append("\n\tFlags: ");
		buffer.append(getFlags());
		return buffer.toString();
	}

}
