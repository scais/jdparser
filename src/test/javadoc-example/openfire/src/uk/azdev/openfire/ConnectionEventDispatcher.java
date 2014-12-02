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
package uk.azdev.openfire;

import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.common.ReceivedInvitation;
import uk.azdev.openfire.common.SessionId;

public class ConnectionEventDispatcher implements ConnectionEventListener {

	private List<ConnectionEventListener> listeners;
	
	public ConnectionEventDispatcher() {
		listeners = new LinkedList<ConnectionEventListener>();
	}
	
	public void addListener(ConnectionEventListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void connectionError() {
		for(ConnectionEventListener listener : listeners) {
			listener.connectionError();
		}
	}

	public synchronized void conversationUpdate(SessionId sessionId) {
		for(ConnectionEventListener listener : listeners) {
			listener.conversationUpdate(sessionId);
		}
	}

	public synchronized void disconnected() {
		for(ConnectionEventListener listener : listeners) {
			listener.disconnected();
		}
	}

	public synchronized void friendsListUpdated() {
		for(ConnectionEventListener listener : listeners) {
			listener.friendsListUpdated();
		}
	}

	public synchronized void loginFailed() {
		for(ConnectionEventListener listener : listeners) {
			listener.loginFailed();
		}
	}

	public void internalError(Exception e) {
		for(ConnectionEventListener listener : listeners) {
			listener.internalError(e);
		}
	}

	public void inviteReceived(ReceivedInvitation invite) {
		for(ConnectionEventListener listener : listeners) {
			listener.inviteReceived(invite);
		}
	}
}
