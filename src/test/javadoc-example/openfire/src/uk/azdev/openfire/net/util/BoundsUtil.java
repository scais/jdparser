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

public class BoundsUtil {

	public static final long MAX_INT32_VALUE = (1L << 32L) - 1L;
	public static final int MAX_INT8_VALUE = (1 << 8) - 1;

	BoundsUtil() {
		throw new RuntimeException("BoundsUtil is not meant to be instantiated");
	}
	
	public static void checkInt32Bounds(long value, String valueName) {
		if(!isInInt32Bounds(value)) {
			throw new IllegalArgumentException(valueName + " outside valid range");
		}
	}
	
	public static boolean isInInt32Bounds(long value) {
		return value >= 0 && value <= MAX_INT32_VALUE;
	}
	
	public static void checkInt8Bounds(int value, String valueName) {
		if(!isInInt8Bounds(value)) {
			throw new IllegalArgumentException(valueName + " outside valid range");
		}
	}
	
	public static boolean isInInt8Bounds(int value) {
		return value >= 0 && value <= MAX_INT8_VALUE;
	}
	
}
