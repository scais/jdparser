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

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import uk.azdev.openfire.common.ActiveGameInfo;
import uk.azdev.openfire.common.GameInfo;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class FriendGameInfoMessage extends StringMapBasedMessage {
	
	private static final String SESSION_ID_LIST_KEY = "sid";
	private static final String GAME_ID_LIST_KEY = "gameid";
	private static final String GAME_IP_LIST_KEY = "gip";
	private static final String GAME_PORT_LIST_KEY = "gport";

	public static final int FRIEND_GAME_INFO_MESSAGE_ID = 135;

	private LinkedHashMap<SessionId, ActiveGameInfo> sidToGameInfoMap;
	
	public FriendGameInfoMessage() {
		sidToGameInfoMap = new LinkedHashMap<SessionId, ActiveGameInfo>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		List<SessionId> sessionIds = map.getAttributeValueAsList(SESSION_ID_LIST_KEY, new SessionIdAttributeValue());
		List<Long> gameIds = map.getAttributeValueAsList(GAME_ID_LIST_KEY, new Int32AttributeValue());
		List<Long> gameIps = map.getAttributeValueAsList(GAME_IP_LIST_KEY, new Int32AttributeValue());
		List<Integer> gamePorts = getInt16ListFromMap(map, GAME_PORT_LIST_KEY);

		Iterator<SessionId> sidIter = sessionIds.iterator();
		Iterator<Long> gameIdsIter = gameIds.iterator();
		Iterator<Long> gameIpsIter = gameIps.iterator();
		Iterator<Integer> gamePortsIter = gamePorts.iterator();
		
		while(sidIter.hasNext()) {
			long gameId = gameIdsIter.next();
			long gameIp = gameIpsIter.next();
			int gamePort = gamePortsIter.next();			
			SessionId sid = sidIter.next();
			
			ActiveGameInfo game;
			if(gameId == 0L) {
				game = null;
			} else {
				InetSocketAddress socketAddr = getSocketAddress(gameIp, gamePort);
				game = new ActiveGameInfo(new GameInfo(gameId), socketAddr);
			}
			
			sidToGameInfoMap.put(sid, game);
		}
	}

	private List<Integer> getInt16ListFromMap(StringKeyedAttributeMap map, String key) {
		List<Long> portsAsInt32 = map.getAttributeValueAsList(key, new Int32AttributeValue());
		List<Integer> portsAsInt16 = new LinkedList<Integer>();
		
		for(Long portAsInt32 : portsAsInt32) {
			portsAsInt16.add(portAsInt32.intValue());
		}
		
		return portsAsInt16;
	}

	private InetSocketAddress getSocketAddress(long address, Integer port) {
		if(address == 0) {
			return null;
		}
		
		// it looks like a recent protocol update is stuffing some extra info
		// into the port field. The first 16 bits of the field are now the
		// port and the second 16 bits are used for something unknown at
		// present
		
		return IOUtil.getInetSocketAddress(address, port & 0xFFFF);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue sidList = new ListAttributeValue();
		ListAttributeValue gameIdList = new ListAttributeValue();
		ListAttributeValue gameIpList = new ListAttributeValue();
		ListAttributeValue gamePortList = new ListAttributeValue();
		
		for(Entry<SessionId, ActiveGameInfo> entry : sidToGameInfoMap.entrySet()) {
			sidList.addValue(new SessionIdAttributeValue(entry.getKey()));
			ActiveGameInfo gameInfo = entry.getValue();
			
			if(gameInfo == null) {
				gameIdList.addValue(new Int32AttributeValue(0));
				gameIpList.addValue(new Int32AttributeValue(0));
				gamePortList.addValue(new Int32AttributeValue(0));
			} else {
				gameIdList.addValue(new Int32AttributeValue(gameInfo.getGameId()));
				
				if(gameInfo.hasGameAddr()) {
					Inet4Address address = (Inet4Address)gameInfo.getGameAddress().getAddress();
					gameIpList.addValue(new Int32AttributeValue(address));
					gamePortList.addValue(new Int32AttributeValue(gameInfo.getGameAddress().getPort()));
				} else {
					gameIpList.addValue(new Int32AttributeValue(0));
					gamePortList.addValue(new Int32AttributeValue(0));
				}
			}
		}
		
		map.addAttribute(SESSION_ID_LIST_KEY, sidList);
		map.addAttribute(GAME_ID_LIST_KEY, gameIdList);
		map.addAttribute(GAME_IP_LIST_KEY, gameIpList);
		map.addAttribute(GAME_PORT_LIST_KEY, gamePortList);
	}

	public int getMessageId() {
		return FRIEND_GAME_INFO_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new FriendGameInfoMessage();
	}
	
	public Set<SessionId> getSessionIdSet() {
		return sidToGameInfoMap.keySet();
	}
	
	public ActiveGameInfo getActiveGameInfoForSid(SessionId sessionId) {
		return sidToGameInfoMap.get(sessionId);
	}

	public void addActiveGameInfo(SessionId sid, ActiveGameInfo info) {
		sidToGameInfoMap.put(sid, info);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Friend Game Info Message");
		
		for(Entry<SessionId, ActiveGameInfo> entry : sidToGameInfoMap.entrySet()) {
			buffer.append("\n\t");
			buffer.append(entry.getKey());
			buffer.append(" -> ");
			if(entry.getValue() == null) {
				buffer.append("no game");
			} else {
				buffer.append(entry.getValue());
			}
		}
		
		return buffer.toString();
	}

}
