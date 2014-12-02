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
package uk.azdev.openfire.conversations;

import java.text.DateFormat;
import java.util.Date;

import uk.azdev.openfire.friendlist.Friend;

public class ConversationLogLine {

	private Friend originator;
	private String message;
	private Date timestamp;
	
	public ConversationLogLine(Friend originator, String message) {
		setOriginator(originator);
		setMessage(message);
		timestamp = new Date();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Friend getOriginator() {
		return originator;
	}

	public void setOriginator(Friend originator) {
		this.originator = originator;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean includeTimestamp) {
		StringBuffer logLine = new StringBuffer();
		if(includeTimestamp) {
			DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
			logLine.append("[");
			logLine.append(formatter.format(timestamp));
			logLine.append("] ");
		}
		logLine.append(originator.getDisplayName());
		logLine.append(": ");
		logLine.append(message);
		logLine.append("\n");
		
		return logLine.toString();
	}
}
