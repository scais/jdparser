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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.azdev.openfire.common.SessionId;

public class PendingFriendUpdateManager {

	private Map<SessionId, List<Runnable>> pendingUpdates;
	
	public PendingFriendUpdateManager() {
		pendingUpdates = new HashMap<SessionId, List<Runnable>>();
	}
	
	public void applyUpdates(SessionId sid) {
		for(Runnable r : getUpdateListForSid(sid)) {
			r.run();
		}
		
		pendingUpdates.remove(sid);
	}
	
	public void queueUpdate(SessionId sid, Runnable update) {
		List<Runnable> updatesForSid = getUpdateListForSid(sid);
		updatesForSid.add(update);
	}

	private List<Runnable> getUpdateListForSid(SessionId sid) {
		if(!pendingUpdates.containsKey(sid)) {
			LinkedList<Runnable> updateList = new LinkedList<Runnable>();
			pendingUpdates.put(sid, updateList);
			return updateList;
		}
		
		return pendingUpdates.get(sid);
	}
	
}
