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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class ChannelWriter {

	private WritableByteChannel channel;
	private ByteBuffer messageBuffer;

	public ChannelWriter(WritableByteChannel outputChannel) {
		this.channel = outputChannel;
		messageBuffer = IOUtil.createBuffer(ProtocolConstants.MAX_MESSAGE_SIZE);
	}

	public void writeOpeningStatement() throws IOException {
		messageBuffer.rewind();
		messageBuffer.limit(ProtocolConstants.CLIENT_OPENING_STATEMENT.length);
		messageBuffer.put(ProtocolConstants.CLIENT_OPENING_STATEMENT);
		messageBuffer.flip();
		
		channel.write(messageBuffer);
	}

	public void writeMessage(IMessage message) throws IOException {
		messageBuffer.rewind();
		messageBuffer.limit(message.getMessageContentSize() + ProtocolConstants.HEADER_SIZE);
		messageBuffer.putShort((short)(message.getMessageContentSize() + ProtocolConstants.HEADER_SIZE));
		messageBuffer.putShort((short)message.getMessageId());
		message.writeMessageContent(messageBuffer);
		messageBuffer.flip();
		
		channel.write(messageBuffer);
	}

	public void close() throws IOException {
		channel.close();
	}

}
