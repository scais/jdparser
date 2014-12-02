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
package uk.azdev.openfire.friendlist;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import uk.azdev.openfire.common.ActiveGameInfo;
import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.common.SessionId;

public class FriendsList {

	private Friend self;
	
	private Map<Long, Friend> friendsById;
	private Map<String, Friend> friendsByUName;
	private Map<SessionId, Friend> onlineFriends;
	
	private Map<Friend, Set<Friend>> friendConnections;
	private Map<Friend, FriendEventDispatcher> friendListeners;
	private PendingFriendUpdateManager pendingUpdateManager;

	private ReentrantLock accessLock;
	
	public FriendsList(Friend self) {
		this.self = self;
		friendsById = new HashMap<Long, Friend>();
		friendsByUName = new HashMap<String, Friend>();
		onlineFriends = new HashMap<SessionId, Friend>();
		friendConnections = new HashMap<Friend, Set<Friend>>();
		friendListeners = new HashMap<Friend, FriendEventDispatcher>();
		pendingUpdateManager = new PendingFriendUpdateManager();
		accessLock = new ReentrantLock();
		addFriend(self);
	}
	
	public void acquireLock() {
		accessLock.lock();
	}
	
	public void releaseLock() {
		accessLock.unlock();
	}

	public boolean containsFriend(long userId) {
		acquireLock();
		try {
			return friendsById.containsKey(userId);
		} finally {
			releaseLock();
		}
	}
	
	public boolean containsFriend(String userName) {
		acquireLock();
		try {
			return friendsByUName.containsKey(userName);
		} finally {
			releaseLock();
		}
	}
	
	private void addOrUpdateFriend(Friend friend) {
		if(friendsById.containsKey(friend.getUserId())) {
			Friend origFriend = friendsById.get(friend.getUserId());
			origFriend.update(friend);
		} else {
			friendsById.put(friend.getUserId(), friend);
			friendConnections.put(friend, new HashSet<Friend>());
			friendListeners.put(friend, new FriendEventDispatcher());
		}
		
		updateOnlineFriendsMap(friend);
		updateFriendsByUNameMap(friend);
	}

	private void updateOnlineFriendsMap(Friend friend) {
		if(friend.isOnline() && !onlineFriends.containsKey(friend.getSessionId())) {
			onlineFriends.put(friend.getSessionId(), friend);
		} else if(!friend.isOnline() && friendsById.get(friend.getUserId()).isOnline()) {
			setFriendOffline(friend.getUserId());
		}
	}
	
	private void updateFriendsByUNameMap(Friend friend) {
		if(!friendsByUName.containsKey(friend.getUserName())) {
			friendsByUName.put(friend.getUserName(), friend);
		}
	}
	
	public void addFriend(Friend newFriend) {
		acquireLock();
		try {
			addOrUpdateFriend(newFriend);
		} finally {
			releaseLock();
		}
	}

	public void addFriend(Friend newFriend, Friend connectedFriend) {
		acquireLock();
		try {
			addOrUpdateFriend(newFriend);
			connect(newFriend, connectedFriend.getUserId());
		} finally {
			releaseLock();
		}
	}
	
	public Friend getFriend(long userId) {
		acquireLock();
		try {
			return friendsById.get(userId).clone();
		} finally {
			releaseLock();
		}
	}
	
	public Object getFriend(String userName) {
		acquireLock();
		try {
			if(!containsFriend(userName)) {
				return null;
			}
			
			return friendsByUName.get(userName).clone();
		} finally {
			releaseLock();
		}
	}
	
	private Friend getInternalFriend(long userId) {
		return friendsById.get(userId);
	}

	public Friend getSelf() {
		acquireLock();
		try {
			return self.clone();
		} finally {
			releaseLock();
		}
	}
	
	public void setSelfOnline(String displayName, SessionId sessionId, long userId, InetSocketAddress netAddr) {
		acquireLock();
		try {
			self.setDisplayName(displayName);
			self.setAddress(netAddr);
			self.setOnline(sessionId);
			self.setUserId(userId);
		} finally {
			releaseLock();
		}
	}

	public void setFriendOnline(long userId, SessionId sessionIdForUser) {
		acquireLock();
		try {
			Friend friend = getInternalFriend(userId);
			friend.setOnline(sessionIdForUser);
			onlineFriends.put(sessionIdForUser, friend);
			pendingUpdateManager.applyUpdates(sessionIdForUser);
			friendListeners.get(friend).friendOnline(friend.clone());
		} finally {
			releaseLock();
		}
	}

	public Friend getOnlineFriend(SessionId sessionId) {
		acquireLock();
		try {
			if(onlineFriends.containsKey(sessionId)) {
				return onlineFriends.get(sessionId).clone();
			}
			
			return null;
		} finally {
			releaseLock();
		}
	}

	public void setFriendOffline(long userId) {
		acquireLock();
		try {
			Friend friend = getInternalFriend(userId);
			SessionId oldSid = friend.getSessionId();
			onlineFriends.remove(oldSid);
			friend.setOffline();
			friendListeners.get(friend).friendOffline(friend.clone());
		} finally {
			releaseLock();
		}
	}
	
	public void updateFriendStatus(SessionId friendSid, String newStatus) {
		acquireLock();
		try {
			Friend f = onlineFriends.get(friendSid);
			if(f == null) {
				Logging.connectionLogger.warning("Status update received for friend when friend is unknown. SID: " + friendSid + " status:" + newStatus);
				return;
			}
			f.setStatus(newStatus);
			friendListeners.get(f).statusChanged(f.clone());
		} finally {
			releaseLock();
		}
	}
	
	public void updateFriendGame(final SessionId friendSid, final ActiveGameInfo game) {
		acquireLock();
		try {
			Friend f = onlineFriends.get(friendSid);
			if(f == null) {
				Logging.connectionLogger.info("Game info update received for friend when friend is unknown. Will apply update once becomes known. SID: " + friendSid + " game:" + game);
				pendingUpdateManager.queueUpdate(friendSid, new Runnable() {
					public void run() {
						updateFriendGame(friendSid, game);
					}
				});
				return;
			}
			Logging.connectionLogger.info("Game info update being applied for " + f.getDisplayName());
			f.setGame(game);
			friendListeners.get(f).gameInfoChanged(f.clone());
		} finally {
			releaseLock();
		}
	}

	public void connect(Friend friend, long friendId) {
		acquireLock();
		try {
			Friend connectedFriend = getOrCreateNew(friendId);
			
			if(friendConnections.get(friend).contains(connectedFriend)) {
				return;
			}
			
			Set<Friend> connections = friendConnections.get(friend);
			connections.add(connectedFriend);
			
			connect(connectedFriend, friend.getUserId());
		} finally {
			releaseLock();
		}
	}
	
	private Friend getOrCreateNew(Long friendId) {
		Friend friend;
		if(friendsById.containsKey(friendId)) {
			friend = friendsById.get(friendId);
		} else {
			friend = new Friend(friendId);
			addFriend(friend);
		}
		
		return friend;
	}
	
	public boolean areConnected(Friend a, Friend b) {
		acquireLock();
		try {
			return friendConnections.get(a).contains(b);
		} finally {
			releaseLock();
		}
	}

	public Set<Friend> getMyFriends() {
		acquireLock();
		try {
			Set<Friend> myFriends = friendConnections.get(self);
			Set<Friend> deepCopiedSet = new HashSet<Friend>();
			for(Friend f : myFriends) {
				deepCopiedSet.add(f.clone());
			}
			
			return deepCopiedSet;
		} finally {
			releaseLock();
		}
	}
	
	public List<Friend> getAllFriends() {
		acquireLock();
		try {
			Collection<Friend> allFriends = friendsById.values();
			List<Friend> clonedFriends = new ArrayList<Friend>(allFriends.size());
			for(Friend f : allFriends) {
				if(f == self) {
					continue;
				}
				clonedFriends.add(f.clone());
			}
			
			return clonedFriends;
		} finally {
			releaseLock();
		}
	}

	public void addFriendListener(Friend forFriend, IFriendListener listener) {
	    friendListeners.get(forFriend).addListener(listener);
	}
	
}
