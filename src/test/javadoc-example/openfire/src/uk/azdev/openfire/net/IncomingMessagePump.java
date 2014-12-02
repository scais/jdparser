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
import java.nio.channels.AsynchronousCloseException;

import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.net.messages.IMessage;

public class IncomingMessagePump extends ConnectionThread {

	private boolean stopOnNoMessagesLeft;
	private ChannelReader reader;
	
	public IncomingMessagePump(boolean stopOnNoMessagesLeft) {
		this.stopOnNoMessagesLeft = stopOnNoMessagesLeft;
	}
	
	public void setReader(ChannelReader reader) {
		this.reader = reader;
	}
	
	@Override
	public void doProcessing() throws IOException {
		try {
			do {
				IMessage message = reader.readMessage();
				if(message != null) {
					Logging.connectionLogger.fine("received message: " + message);
					dispatchMessageToListeners(message);
				} else if(stopOnNoMessagesLeft) {
					return;
				}
			} while(!plannedStop);
		} catch(AsynchronousCloseException e) {
			if(!plannedStop) {
				throw e;
			}
		}
	}
}
