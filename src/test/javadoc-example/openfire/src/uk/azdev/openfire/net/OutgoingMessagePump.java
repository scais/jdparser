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

import java.nio.channels.AsynchronousCloseException;
import java.util.LinkedList;

import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.net.messages.IMessage;

public class OutgoingMessagePump extends ConnectionThread {

	private LinkedList<IMessage> queuedMessages;
	private Object emptyQueueMutex;
	private ChannelWriter writer;
	
	public OutgoingMessagePump() {
		queuedMessages = new LinkedList<IMessage>();
		emptyQueueMutex = new Object();
	}
	
	public void setWriter(ChannelWriter writer) {
		this.writer = writer;
	}
	
	@Override
	public void setPlannedStop() throws InterruptedException {
		super.setPlannedStop();
		synchronized (queuedMessages) {
			queuedMessages.notify();
		}
		
	}
	
	@Override
	protected void doProcessing() throws Exception {
		try {
			writer.writeOpeningStatement();
			while(!plannedStop) {
				IMessage nextMessage = getNextMessage();
				
				if(nextMessage != null) {
					Logging.connectionLogger.fine("sending message: " + nextMessage);
					writer.writeMessage(nextMessage);
				}
			}
		} catch(AsynchronousCloseException e) {
			if(!plannedStop) {
				throw e;
			}
			
		} finally {
			synchronized (emptyQueueMutex) {
				emptyQueueMutex.notifyAll();
			}
		}
	}
	
	private IMessage getNextMessage() throws InterruptedException {
		IMessage message = null;
		while(message == null && !plannedStop) {
			synchronized (queuedMessages) {
				if(queuedMessages.size() > 0) {
					message = queuedMessages.removeFirst();
				} else {
					synchronized (emptyQueueMutex) {
						emptyQueueMutex.notifyAll();
					}
					queuedMessages.wait();
				}
			}
		}
		
		return message;
	}

	public void sendMessage(IMessage message) {
		synchronized(queuedMessages) {
			queuedMessages.addLast(message);
			if(queuedMessages.size() == 1) {
				queuedMessages.notify();
			}
		}
	}
	
	public void waitForEmptyQueue() throws InterruptedException {
		boolean empty = false;
		synchronized (queuedMessages) {
			empty = (queuedMessages.size() == 0);
		}
		
		if(!empty) {
			synchronized (emptyQueueMutex) {
				emptyQueueMutex.wait();
			}
		}
	}
}
