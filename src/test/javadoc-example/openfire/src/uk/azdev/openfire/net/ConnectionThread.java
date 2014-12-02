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

import java.util.ArrayList;
import java.util.List;

import uk.azdev.openfire.net.messages.IMessage;

public abstract class ConnectionThread implements Runnable {

	private List<ConnectionStateListener> listeners;
	protected boolean plannedStop;
	private Thread associatedThread;
	
	public ConnectionThread() {
		this.listeners = new ArrayList<ConnectionStateListener>();
		reset();
	}
	
	public void reset() {
		plannedStop = false;
	}
	
	public void waitForExit() throws InterruptedException {
		associatedThread.join();
	}
	
	public void setPlannedStop() throws InterruptedException {
		plannedStop = true;
	}
	
	public void start(String threadName) {
		associatedThread = new Thread(this, threadName);
		associatedThread.setDaemon(true);
		associatedThread.start();
	}
	
	public void run() {
		try {
			doProcessing();
			if(!plannedStop) {
				throw new RuntimeException("unexpected termination of connection thread");
			}
		} catch(Exception e) {
			notifyConnectionError(e);
		}
	}
	
	public void addStateListener(ConnectionStateListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	protected void dispatchMessageToListeners(IMessage message) {
		synchronized(listeners) {
			for(ConnectionStateListener messageListener : listeners) {
				messageListener.messageReceived(message);
			}
		}
	}
	
	protected void notifyConnectionError(Exception e) {
		synchronized(listeners) {
			for(ConnectionStateListener listener : listeners) {
				listener.connectionError(e);
			}
		}
	}
	
	public Thread getAssociatedThread() {
		return associatedThread;
	}

	protected abstract void doProcessing() throws Exception;
}
