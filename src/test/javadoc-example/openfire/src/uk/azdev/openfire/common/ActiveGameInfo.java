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

import java.net.InetSocketAddress;

public class ActiveGameInfo {
	
	private GameInfo gameInfo;
	private InetSocketAddress gameAddr;
	
	public ActiveGameInfo(GameInfo gameInfo, InetSocketAddress gameAddr) {
		this.gameInfo = gameInfo;
		this.gameAddr = gameAddr;
	}

	public long getGameId() {
		return gameInfo.getId();
	}
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}
	
	public void setGameInfo(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}
	
	public InetSocketAddress getGameAddress() {
		return gameAddr;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ActiveGameInfo)) {
			return false;
		}
		
		ActiveGameInfo other = (ActiveGameInfo)o;
		
		return objEqual(this.gameInfo, other.gameInfo) && objEqual(this.gameAddr, other.gameAddr);
	}
	
	private boolean objEqual(Object a, Object b) {
		return (a == null && b == null) || (a != null && a.equals(b));
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		gameInfo.toString(buffer);
		
		if(gameAddr == null) {
			buffer.append(" (no server info)");
		} else {
			buffer.append(" @ ");
			buffer.append(gameAddr);
		}
		
		return buffer.toString();
	}

	public boolean hasGameAddr() {
		return gameAddr != null;
	}

	
}
