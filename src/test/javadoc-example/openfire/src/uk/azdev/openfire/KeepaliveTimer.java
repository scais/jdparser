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

import java.util.Timer;
import java.util.TimerTask;

import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.outgoing.KeepaliveMessage;

public class KeepaliveTimer {

	private static final long DEFAULT_KEEPALIVE_INTERVAL_IN_MS = 60000;
	
	private IMessageSender sender;
	private Timer sendTimer;
	private KeepaliveSender task;
	private long interval;
	
	public KeepaliveTimer(IMessageSender sender) {
		this(sender, DEFAULT_KEEPALIVE_INTERVAL_IN_MS);
	}
	
	public KeepaliveTimer(IMessageSender sender, long interval) {
		this.sender = sender;
		this.interval = interval;
	}
	
	public void start() {
		sendTimer = new Timer(true);
		scheduleTask();
	}
	
	public void stop() {
		sendTimer.cancel();
	}
	
	public void resetTimer() {
		task.cancel();
		scheduleTask();
	}

	private void scheduleTask() {
		task = new KeepaliveSender();
		sendTimer.scheduleAtFixedRate(task, interval, interval);
	}
	
	private class KeepaliveSender extends TimerTask {
		@Override
		public void run() {
			sender.sendMessage(new KeepaliveMessage());
		}
	}
}
