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
import java.io.InputStream;
import java.util.Properties;

import uk.azdev.openfire.net.ProtocolConstants;

public class OpenFireConfiguration {
	
	public static final String DEFAULT_XFIRE_GAMES_INI_PATH = "xfire_games.ini";
	public static final String DEFAULT_USER_NAME = "";
	public static final String DEFAULT_PASSWORD = "";
	public static final int DEFAULT_NETWORK_PORT= 50000;
	public static final int DEFAULT_LOCAL_PORT = 50000;
	public static final String DEFAULT_LONG_VERSION = "3.2.0.0";
	public static final long DEFAULT_SHORT_VERSION = 94;
	public static final String DEFAULT_CLIENT_LANGUAGE = "us";
	public static final String[] DEFAULT_SKIN_LIST = { "XFire", "standard", "Separator", "XF_URL" }; 
	public static final String DEFAULT_ACTIVE_SKIN = "XFire";
	public static final String DEFAULT_ACTIVE_THEME = "default";
	public static final String DEFAULT_PARTNER = "";
	public static final String DEFAULT_XFIRE_SERVER_HOSTNAME = ProtocolConstants.XFIRE_SERVER_NAME;
	public static final int DEFAULT_XFIRE_SERVER_PORT = ProtocolConstants.XFIRE_SERVER_PORT;
	
	private String xfireGamesIniPath = DEFAULT_XFIRE_GAMES_INI_PATH;
	private String username = DEFAULT_USER_NAME;
	private String password = DEFAULT_PASSWORD;
	private int networkPort = DEFAULT_NETWORK_PORT;
	private int localPort = DEFAULT_LOCAL_PORT;
	private String longVersion = DEFAULT_LONG_VERSION;
	private long shortVersion = DEFAULT_SHORT_VERSION;
	private String clientLanguage = DEFAULT_CLIENT_LANGUAGE;
	private String[] skinList =  DEFAULT_SKIN_LIST;
	private String activeSkin = DEFAULT_ACTIVE_SKIN;
	private String activeTheme = DEFAULT_ACTIVE_THEME;
	private String partner = DEFAULT_PARTNER;
	private String xfireServerHostName = DEFAULT_XFIRE_SERVER_HOSTNAME;
	private int xfireServerPortNum = DEFAULT_XFIRE_SERVER_PORT;
	
	public String getXfireGamesIniPath() {
		return xfireGamesIniPath;
	}
	
	public void setXfireGamesIniPath(String xfireGamesIniPath) {
		this.xfireGamesIniPath = xfireGamesIniPath;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLongVersion() {
		return longVersion;
	}
	
	public void setLongVersion(String longVersion) {
		this.longVersion = longVersion;
	}

	public long getShortVersion() {
		return shortVersion;
	}
	
	public void setShortVersion(long shortVersion) {
		this.shortVersion = shortVersion;
	}

	public String getClientLanguage() {
		return clientLanguage;
	}
	
	public void setClientLanguage(String clientLanguage) {
		this.clientLanguage = clientLanguage;
	}

	public String getActiveSkin() {
		return activeSkin;
	}
	
	public void setActiveSkin(String activeSkin) {
		this.activeSkin = activeSkin;
	}

	public String getActiveTheme() {
		return activeTheme;
	}
	
	public void setActiveTheme(String activeTheme) {
		this.activeTheme = activeTheme;
	}

	public String getPartner() {
		return partner;
	}
	
	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String[] getSkinList() {
		return skinList;
	}
	
	public void setSkinList(String[] skinList) {
		this.skinList = skinList;
	}

	public String getXfireServerHostName() {
		return xfireServerHostName;
	}
	
	public void setXfireServerHostName(String xfireServerHostName) {
		this.xfireServerHostName = xfireServerHostName;
	}
	
	public int getXfireServerPortNum() {
		return xfireServerPortNum;
	}
	
	public void setXfireServerPortNum(int xfireServerPortNum) {
		this.xfireServerPortNum = xfireServerPortNum;
	}
	
	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public int getNetworkPort() {
		return networkPort;
	}

	public void setNetworkPort(int netPort) {
		this.networkPort = netPort;
	}

	public static OpenFireConfiguration readConfig(InputStream configReader) throws IOException, InvalidConfigurationException {
		Properties properties = new Properties();
		properties.load(configReader);
		return readConfig(properties);
	}
	
	public static OpenFireConfiguration readConfig(Properties properties) throws InvalidConfigurationException {
		OpenFireConfiguration config = new OpenFireConfiguration();
		
		config.setUsername(getMandatoryProp(properties, "username", "client username"));
		config.setPassword(getMandatoryProp(properties, "password", "client password"));
		
		config.setNetworkPort(getMandatoryPropAsBoundedInt(properties, "net.port", "public network port", 1, 65535));
		
		config.setLocalPort(getMandatoryPropAsBoundedInt(properties, "local.port", "local port", 1, 65535));
		config.setXfireGamesIniPath(getMandatoryProp(properties, "games.ini.path", "xfire games ini path"));

		config.setShortVersion(getOptionalPropAsBoundedLong(properties, "client.version", "client version", DEFAULT_SHORT_VERSION, 0, (1L << 32L) - 1L));
		config.setLongVersion(getOptionalProp(properties, "client.long.version", DEFAULT_LONG_VERSION));
		config.setClientLanguage(getOptionalProp(properties, "client.language", DEFAULT_CLIENT_LANGUAGE));
		config.setSkinList(getOptionalPropAsStringList(properties, "client.skinlist", DEFAULT_SKIN_LIST));
		config.setActiveSkin(getOptionalProp(properties, "client.activeskin", DEFAULT_ACTIVE_SKIN));
		config.setActiveTheme(getOptionalProp(properties, "client.activetheme", DEFAULT_ACTIVE_THEME));
		config.setPartner(getOptionalProp(properties, "partner", DEFAULT_PARTNER));
		config.setXfireServerHostName(getOptionalProp(properties, "server.host", DEFAULT_XFIRE_SERVER_HOSTNAME));
		config.setXfireServerPortNum(getOptionalPropAsBoundedInt(properties, "server.port", "xfire server port", DEFAULT_XFIRE_SERVER_PORT, 1, 65535));
		
		return config;
	}

	private static int getMandatoryPropAsBoundedInt(Properties properties, String key, String propDesc, int minVal, int maxVal) throws InvalidConfigurationException {
		String value = getMandatoryProp(properties, key, propDesc);
		
		try {
			int propVal = Integer.parseInt(value);
			if(propVal < minVal || propVal > maxVal) {
				throw new InvalidPropertyValueException(propDesc + " (" + key + ") is not in the legal range (" + minVal + "-" + maxVal + ")");
			}
			
			return propVal;
		} catch(NumberFormatException e) {
			throw new InvalidPropertyValueException(propDesc + " (" + key + ") is not a valid integer");
		}
	}
	
	private static String getMandatoryProp(Properties properties, String key, String propDesc) throws MissingMandatoryPropertyException {
		if(properties.getProperty(key) == null) {
			throw new MissingMandatoryPropertyException(key, propDesc);
		}
		
		return properties.getProperty(key);
	}
	
	private static long getOptionalPropAsBoundedLong(Properties properties, String key, String propDesc, long defaultVal, long minVal, long maxVal) throws InvalidPropertyValueException {
		String value = properties.getProperty(key);
		if(value == null) {
			return defaultVal;
		}
		
		try {
			long longVal = Long.parseLong(value);
			if(longVal < minVal || longVal > maxVal) {
				throw new InvalidPropertyValueException(propDesc + " (" + key + ") is not in the legal range (" + minVal + "-" + maxVal + ")");
			}
			return longVal;
		} catch(NumberFormatException e) {
			throw new InvalidPropertyValueException(propDesc + " (" + key + ") is not a valid integer");
		}
	}
	
	private static int getOptionalPropAsBoundedInt(Properties properties, String key, String propDesc, int defaultVal, int minVal, int maxVal) throws InvalidPropertyValueException {
		String value = properties.getProperty(key);
		if(value == null) {
			return defaultVal;
		}
		
		try {
			int propVal = Integer.parseInt(value);
			if(propVal < minVal || propVal > maxVal) {
				throw new InvalidPropertyValueException(propDesc + " (" + key + ") is not in the legal range (" + minVal + "-" + maxVal + ")");
			}
			return propVal;
		} catch(NumberFormatException e) {
			throw new InvalidPropertyValueException(propDesc + " (" + key + ") is not a valid integer");
		}
	}
	
	private static String[] getOptionalPropAsStringList(Properties properties, String key, String[] defaultVal) {
		String value = properties.getProperty(key);
		if(value == null) {
			return defaultVal;
		}
		
		String[] segments = value.split(",");
		
		for(int i=0; i < segments.length; i++) {
			segments[i] = segments[i].trim();
		}
		
		return segments;
	}
	
	private static String getOptionalProp(Properties properties, String key, String defaultVal) {
		String value = properties.getProperty(key);
		if(value == null) {
			return defaultVal;
		}
		
		return value;
	}
}
