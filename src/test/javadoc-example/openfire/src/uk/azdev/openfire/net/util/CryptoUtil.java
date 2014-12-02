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
package uk.azdev.openfire.net.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {
	
	private static final String ULTIMATE_ARENA_POSTFIX = "UltimateArena";
	
	CryptoUtil() {
		throw new RuntimeException("CryptoUtil is not meant to be instantiated");
	}
	
	public static String getHashedPassword(String username, String password, String salt) {
		String userPassHash = sha1HashAsHexString(username + password + ULTIMATE_ARENA_POSTFIX);
		return sha1HashAsHexString(userPassHash + salt);
	}
	
	public static String sha1HashAsHexString(String s) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.update(IOUtil.encodeString(s));
			return IOUtil.printByteArray(sha1.digest(), false, false);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No cryptographic provider for SHA-1 configured");
		}
	}

}
