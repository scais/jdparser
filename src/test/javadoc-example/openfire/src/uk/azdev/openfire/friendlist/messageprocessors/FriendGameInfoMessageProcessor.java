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

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.common.ActiveGameInfo;
import uk.azdev.openfire.common.GameInfo;
import uk.azdev.openfire.common.GameInfoMap;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.FriendGameInfoMessage;

public class FriendGameInfoMessageProcessor implements IMessageProcessor {

	private FriendsList friendList;
	private ConnectionEventListener listener;
	private GameInfoMap gameTypeLookup;

	public FriendGameInfoMessageProcessor(FriendsList friendList, ConnectionEventListener listener, GameInfoMap gameTypeLookup) {
		this.friendList = friendList;
		this.listener = listener;
		this.gameTypeLookup = gameTypeLookup;
	}

	public void processMessage(IMessage message) {
		FriendGameInfoMessage gameInfoMessage = (FriendGameInfoMessage) message;
		
		for(SessionId sid : gameInfoMessage.getSessionIdSet()) {
			ActiveGameInfo activeGameInfoForSid = gameInfoMessage.getActiveGameInfoForSid(sid);
			resolveGameInfo(activeGameInfoForSid);
			friendList.updateFriendGame(sid, activeGameInfoForSid);
		}
		
		listener.friendsListUpdated();
	}

	private void resolveGameInfo(ActiveGameInfo activeGameInfo) {
		if(activeGameInfo != null) {
			GameInfo resolvedGame = gameTypeLookup.getGameById(activeGameInfo.getGameId());
			if(resolvedGame != null) {
				activeGameInfo.setGameInfo(resolvedGame);
			}
		}
	}

}
