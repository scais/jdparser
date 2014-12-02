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
package uk.azdev.openfire.common;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GameInfoMap {
	
	private static final String SHORT_NAME_KEY = "ShortName";
	private static final String LONG_NAME_KEY = "LongName";
	private static final String SOFTWARE_TYPE_KEY = "SoftwareType";
	private Map<Long, GameInfo> gameInfoById;
	
	public GameInfoMap() {
		gameInfoById = new HashMap<Long, GameInfo>();
	}

	public void addGameInfo(GameInfo gameInfo) {
		gameInfoById.put(gameInfo.getId(), gameInfo);
	}

	public GameInfo getGameById(long id) {
		return gameInfoById.get(id);
	}
	
	public void loadFromXfireGamesIni(Reader iniReader) throws IOException {
		IniData iniContents = new IniData();
		iniContents.read(iniReader);
		
		List<String> keys = iniContents.getAllSectionNames();
		Iterator<String> keysIter = keys.iterator();
		
		while(keysIter.hasNext()) {
			String key = keysIter.next();
			try {
				long gameId = Long.parseLong(key);
				if(gameId <= 0) {
					continue;
				}
				
				if(iniContents.hasPropertyInSection(key, SOFTWARE_TYPE_KEY)) {
					// sections with the SoftwareType key are not games
					continue;
				}
				
				String longGameName = iniContents.getStringProperty(key, LONG_NAME_KEY);
				String shortGameName = iniContents.getStringProperty(key, SHORT_NAME_KEY);
				GameInfo info = new GameInfo(gameId, longGameName, shortGameName);
				gameInfoById.put(gameId, info);
			} catch(NumberFormatException e) {
				// this isn't a game section, ignore it
			}
		}
	}

}
