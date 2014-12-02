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

import java.net.Inet4Address;

public class ActiveApplicationInfo {

    private long appId;
    private Inet4Address address;
    
    public ActiveApplicationInfo(long appId, Inet4Address address) {
        this.appId = appId;
        this.address = address;
    }
    
    public long getAppId() {
        return appId;
    }
    
    public Inet4Address getAddress() {
        return address;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof ActiveApplicationInfo)) {
    		return false;
    	}
    	
    	ActiveApplicationInfo other = (ActiveApplicationInfo) obj;
    	return other.appId == this.appId && addressesEqual(this.address, other.address);
    }
    
    private boolean addressesEqual(Inet4Address a, Inet4Address b) {
		if(a == null) {
			return b == null;
		}
		
		return a.equals(b);
	}

	@Override
    public int hashCode() {
    	return new Long(appId).hashCode() ^ address.hashCode();
    }
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ActiveApplicationInfo(appId=");
		buffer.append(appId);
		buffer.append(",addr=");
		buffer.append(address);
		buffer.append(")");
		
		return buffer.toString();
	}
    
}
