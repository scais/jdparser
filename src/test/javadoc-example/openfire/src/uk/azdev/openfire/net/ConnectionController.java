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
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import uk.azdev.openfire.net.messages.IMessage;

public class ConnectionController implements IConnectionController {

	private SocketChannel channel;
	private IncomingMessagePump inPump;
	private OutgoingMessagePump outPump;
	private String xfireServerHostName;
	private int xfireServerPortNum;
	
	public ConnectionController(String xfireServerHostName, int xfireServerPortNum) {
		inPump = new IncomingMessagePump(false);
		outPump = new OutgoingMessagePump();
		this.xfireServerHostName = xfireServerHostName;
		this.xfireServerPortNum = xfireServerPortNum;
	}
	
	public void start() throws UnknownHostException, IOException {
		channel = SocketChannel.open(new InetSocketAddress(xfireServerHostName, xfireServerPortNum));
		channel.configureBlocking(true);
		
		ChannelReader reader = new ChannelReader(channel);
		inPump.reset();
		inPump.setReader(reader);
		
		ChannelWriter writer = new ChannelWriter(channel);
		outPump.reset();
		outPump.setWriter(writer);
		
		inPump.start("InPump");
		outPump.start("OutPump");
	}

	public void stop() throws InterruptedException, IOException {
		outPump.waitForEmptyQueue();
		inPump.setPlannedStop();
		outPump.setPlannedStop();
		channel.close();
		inPump.waitForExit();
		outPump.waitForExit();
	}
	
	public void addStateListener(ConnectionStateListener listener) {
		inPump.addStateListener(listener);
		outPump.addStateListener(listener);
	}
	
	public void sendMessage(IMessage message) {
		outPump.sendMessage(message);
	}

	public boolean ownsCurrentThread() {
		return Thread.currentThread().equals(inPump.getAssociatedThread())
		    || Thread.currentThread().equals(outPump.getAssociatedThread());
	}
}
