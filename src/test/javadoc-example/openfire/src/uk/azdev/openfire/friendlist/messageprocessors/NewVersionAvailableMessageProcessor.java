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
package uk.azdev.openfire.friendlist.messageprocessors;

import uk.azdev.openfire.ConnectionManipulator;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.XFireUpdate;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.NewVersionAvailableMessage;

public class NewVersionAvailableMessageProcessor implements IMessageProcessor {

	private ConnectionManipulator connectionManipulator;
	private OpenFireConfiguration config;

	public NewVersionAvailableMessageProcessor(ConnectionManipulator connection, OpenFireConfiguration config) {
		this.connectionManipulator = connection;
		this.config = config;
	}

	public void processMessage(IMessage msg) {
		NewVersionAvailableMessage message = (NewVersionAvailableMessage)msg;
		long highestVersion = config.getShortVersion();
		for(XFireUpdate update : message.getNewVersionsList()) {
			if(update.getVersionNum() > highestVersion) {
				highestVersion = update.getVersionNum();
			}
		}
		
		config.setShortVersion(highestVersion);
		connectionManipulator.reconnect();
	}
}