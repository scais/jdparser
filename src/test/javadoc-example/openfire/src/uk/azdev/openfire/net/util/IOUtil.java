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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.security.SecureRandom;

import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.net.ProtocolConstants;

public class IOUtil {

	private static final Charset UTF8_ENCODING = Charset.forName("UTF-8");
	private static final SecureRandom randomGen = new SecureRandom();
	
	IOUtil() {
		throw new RuntimeException("IOUtil is not meant to be instantiated");
	}
	
	public static int readUnsignedByte(ByteBuffer buffer) {
		return convertByteToUnsigned(buffer.get());
	}
	
	public static int convertByteToUnsigned(byte b) {
		return b & 0xFF;
	}
	
	public static int readUnsignedShort(ReadableByteChannel channel) throws IOException {
		ByteBuffer readBuffer = createBuffer(2);
		readAllBytesOrFail(channel, readBuffer, 2);
		return readUnsignedShort(readBuffer);
	}
	
	public static int readUnsignedShort(ByteBuffer buffer) {
		return buffer.getShort() & 0xFFFF;
	}
	
	public static long readUnsignedInt(ReadableByteChannel channel) throws IOException {
		ByteBuffer readBuffer = createBuffer(4);
		readAllBytesOrFail(channel, readBuffer, 4);
		return readUnsignedInt(readBuffer);
	}
	
	public static long readUnsignedInt(ByteBuffer buffer) {
		return buffer.getInt() & 0xFFFFFFFFL;
	}
	
	public static void readAllBytesOrFail(ReadableByteChannel channel, ByteBuffer buffer, int numBytes) throws IOException {
		if(!readAllBytesOrNone(channel, buffer, numBytes)) {
			throw new IOException("no more bytes left in channel to read");
		}
	}
	
	public static boolean readAllBytesOrNone(ReadableByteChannel channel, ByteBuffer buffer, int numBytes) throws IOException {
		buffer.rewind();
		buffer.limit(numBytes);
		
		int totalBytesRead = 0;
		int bytesRead = channel.read(buffer);
		if(bytesRead <= 0) {
			// no bytes available at all
			return false;
		}
		totalBytesRead += bytesRead;
		while(totalBytesRead < numBytes) {
			bytesRead = channel.read(buffer);
			if(bytesRead == -1) {
				throw new IOException("insufficient but non-zero amount of bytes left in channel");
			}
			
			totalBytesRead += bytesRead;
		}
		
		buffer.flip();
		
		return true;
	}
	
	public static boolean nextBytesMatchArray(ByteBuffer buffer, byte[] bytes) {
		if(buffer.remaining() != bytes.length) {
			return false;
		}
		
		for(byte b : bytes) {
			if(buffer.get() != b) {
				return false;
			}
		}
		
		return true;
	}
	
	public static int getEncodedStringSize(String str) {
		return encodeString(str).length;
	}
	
	public static byte[] encodeString(String str) {
		try {
			return str.getBytes(UTF8_ENCODING.name());
		} catch (UnsupportedEncodingException e) {
			// UTF-8 should always be supported, so this cannot occur
			Logging.connectionLogger.severe("UTF-8 encoding is not supported!");
			return null;
		}
	}
	
	public static String decodeString(byte[] stringBytes) {
		try {
			return new String(stringBytes, UTF8_ENCODING.name());
		} catch (UnsupportedEncodingException e) {
			// UTF-8 should always be supported, so this cannot occur
			Logging.connectionLogger.severe("UTF-8 encoding is not supported!");
			return null;
		}
	}
	
	public static String printByteArray(byte[] bytes) {
		return printByteArray(bytes, true, true);
	}
	
	public static String printByteArray(byte[] bytes, boolean spacesBetweenBytes, boolean upperCase) {
		StringBuffer buffer = new StringBuffer(bytes.length * 3);
		
		for(int i=0; i < bytes.length; i++) {
			int asUnsigned= convertByteToUnsigned(bytes[i]);
			String hexString = Integer.toHexString(asUnsigned);
			if(hexString.length() == 1) {
				hexString = "0" + hexString;
			}
			
			if(upperCase) {
				hexString = hexString.toUpperCase();
			}
			buffer.append(hexString);
			
			if(spacesBetweenBytes && i < bytes.length - 1) {
				buffer.append(' ');
			}
		}
		
		return buffer.toString();
	}
	
	public static ByteBuffer createBuffer(int size) {
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		return buffer;
	}
	
	public static Inet4Address getAddressFromInt32(long value) {
		byte[] address = new byte[4];
		address[0] = (byte)(value >> 24);
		address[1] = (byte)(value >> 16);
		address[2] = (byte)(value >> 8);
		address[3] = (byte)(value);
		try {
			return (Inet4Address)InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			// will never happen as we know the byte array we have passed
			// is the correct length for IPv4
			throw new RuntimeException("UnknownHostException unexpectedly thrown", e);
		}
	}
	
	public static Inet4Address getInet4Address(String textIpRepresentation) {
	    byte[] address = new byte[4];
	    String[] segments = textIpRepresentation.split("\\.");
	    
	    if(segments.length != 4) {
	        throw new IllegalArgumentException("Passed textual IP \"" + textIpRepresentation + "\" is not of form a.b.c.d");
	    }
	    
	    for(int i=0; i < segments.length; i++) {
	        try {
	            address[i] = (byte)Integer.parseInt(segments[i]);
	        } catch(NumberFormatException e) {
	            throw new IllegalArgumentException("component of textual IP could not be parsed", e);
	        }
	    }
	    
	    try {
            return (Inet4Address) InetAddress.getByAddress(textIpRepresentation, address);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Textual IP could not be used", e);
        }
	}
	
	public static InetSocketAddress getInetSocketAddress(long addrAsInt32, int port) {
		return new InetSocketAddress(getAddressFromInt32(addrAsInt32), port);
	}
	
	public static long getInt32FromAddress(Inet4Address inetAddress) {
		byte[] addrBytes = inetAddress.getAddress();
		return  ((long)convertByteToUnsigned(addrBytes[0]) << 24L) 
		      | ((long)convertByteToUnsigned(addrBytes[1]) << 16L) 
		      | ((long)convertByteToUnsigned(addrBytes[2]) << 8L)
		      | (convertByteToUnsigned(addrBytes[3]));
	}
	
	public static String generateSaltString() {
		byte[] saltBytes = generateSalt();
		return IOUtil.printByteArray(saltBytes, false, false);
	}

	private static byte[] generateSalt() {
		byte[] saltBytes = new byte[ProtocolConstants.SALT_SIZE_IN_BYTES];
		randomGen.nextBytes(saltBytes);
		return saltBytes;
	}
}
