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

/**
 * Represents an abstracted message type that can be sent or
 * received. Implementors of this interface will typically provide
 * message-specific interfaces to make construction and analysis
 * of messages easier.
 * @author Iain
 *
 */
public interface IMessage {
	
	public int getMessageId();
	
	/**
	 * Creates a raw message for transmission.
	 */
	public void readMessageContent(ByteBuffer buffer);
	
	/**
	 * Extracts the necessary data from the provided raw message.
	 */
	public void writeMessageContent(ByteBuffer buffer);
	
	/**
	 * Determines the size of the message as it would appear
	 * on a binary stream.
	 */
	public int getMessageContentSize();
	
	/**
	 * Creates a clean instance of the same type.
	 */
	public IMessage newInstance();

}
