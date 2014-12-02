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
package uk.azdev.openfire.net.messages.incoming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.common.Invitation;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class IncomingInvitationMessage extends StringMapBasedMessage {

	public static final int TYPE_ID = 138;
	
	private static final String USERNAMES_KEY = "name";
	private static final String DISPLAYNAMES_KEY = "nick";
	private static final String INVITE_MESSAGES_KEY = "msg";
	
	private List<Invitation> invitations;
	
	public IncomingInvitationMessage() {
		invitations = new LinkedList<Invitation>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		Iterator<String> userNames = map.getAttributeValueAsList(USERNAMES_KEY, new StringAttributeValue()).iterator();
		Iterator<String> displayNames = map.getAttributeValueAsList(DISPLAYNAMES_KEY, new StringAttributeValue()).iterator();
		Iterator<String> inviteMessages = map.getAttributeValueAsList(INVITE_MESSAGES_KEY, new StringAttributeValue()).iterator();
		
		while(userNames.hasNext()) {
			invitations.add(new Invitation(userNames.next(), displayNames.next(), inviteMessages.next()));
		}
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue userNames = new ListAttributeValue();
		ListAttributeValue displayNames = new ListAttributeValue();
		ListAttributeValue inviteMessages = new ListAttributeValue();
		
		for(Invitation invite : invitations) {
			userNames.addValue(new StringAttributeValue(invite.getUserName()));
			displayNames.addValue(new StringAttributeValue(invite.getDisplayName()));
			inviteMessages.addValue(new StringAttributeValue(invite.getMessage()));
		}
		
		map.addAttribute(USERNAMES_KEY, userNames);
		map.addAttribute(DISPLAYNAMES_KEY, displayNames);
		map.addAttribute(INVITE_MESSAGES_KEY, inviteMessages);
	}

	public int getMessageId() {
		return TYPE_ID;
	}

	public IMessage newInstance() {
		return new IncomingInvitationMessage();
	}

	public List<Invitation> getInvites() {
		return invitations;
	}

	public void addInvite(Invitation invite) {
		invitations.add(invite);
	}

}
