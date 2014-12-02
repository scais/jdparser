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

public class Invitation {

	private String userName;
	private String displayName;
	private String message;
	
	public Invitation(String userName, String displayName, String message) {
		this.userName = userName;
		this.displayName = displayName;
		this.message = message;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getMessage() {
		return message;
	}

	public String getUserName() {
		return userName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Invitation)) {
			return false;
		}
		
		Invitation i = (Invitation)obj;
		return i.getUserName().equals(getUserName())
		    && i.getDisplayName().equals(getDisplayName())
		    && i.getMessage().equals(getMessage());
	}
	
	@Override
	public int hashCode() {
		return getUserName().hashCode() ^ getDisplayName().hashCode() ^ message.hashCode();
	}
}
