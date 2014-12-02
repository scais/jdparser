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
package uk.azdev.openfire.net;

/**
 * Container for common constants used in the xfire protocol.
 */
public final class ProtocolConstants {

	ProtocolConstants() {
		throw new RuntimeException("ProtocolConstants is not meant to be instantiated");
	}
	
	public static final String XFIRE_SERVER_NAME = "cs.xfire.com";
	public static final int XFIRE_SERVER_PORT = 25999;
	
	public static final int MAX_MESSAGE_SIZE = (1 << 16) - 1; // 64KB
	public static final int HEADER_SIZE = 4; // bytes
	
	public static final byte[] CLIENT_OPENING_STATEMENT = { 0x55, 0x41, 0x30, 0x31 }; // "UA01"
	public static final int SALT_SIZE_IN_BYTES = 20;
	public static final long CLIENT_INFO_TRANSMIT_INTERVAL = 5000;
		
}
