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

public class GameInfo {
	
	private long id;
	private String name;
	private String shortName;
	
	public GameInfo(long id, String name, String shortName) {
		this.id = id;
		this.name = name;
		this.shortName = shortName;
	}
	
	public GameInfo(long id) {
		this(id, null, null);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof GameInfo)) {
			return false;
		}
		
		GameInfo otherInfo = (GameInfo)obj;
		return otherInfo.getId() == this.id;
	}
	
	@Override
	public int hashCode() {
		return (int)(id & 0xFFFFFFFFL);
	}

	public void toString(StringBuffer buffer) {
		if(name != null) {
			buffer.append(name);
		} else {
			buffer.append("GID");
			buffer.append(id);
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		toString(buffer);
		
		return buffer.toString();
	}
}
