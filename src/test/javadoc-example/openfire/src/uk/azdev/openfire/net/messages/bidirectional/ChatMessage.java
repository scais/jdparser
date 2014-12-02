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
package uk.azdev.openfire.net.messages.bidirectional;

import java.net.Inet4Address;
import java.net.InetSocketAddress;

import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.attrvalues.StringKeyedMapAttributeValue;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class ChatMessage extends StringMapBasedMessage {

	public static final int TYPE_ID = 2;
	
	private static final int CONTENT_MESSAGE_TYPE = 0;
	private static final int ACK_MESSAGE_TYPE = 1;
	private static final int CLIENT_INFO_MESSAGE_TYPE = 2;
	private static final int USER_TYPING_MESSAGE_TYPE = 3;
	
	// top level keys
	private static final String SID_KEY = "sid";
	private static final String PEER_MESSAGE_KEY = "peermsg";
	
	// identifies the other keys we can expect within the peermsg map
	private static final String MESSAGE_TYPE_KEY = "msgtype";
	
	// other potential keys within the peerMsg
	private static final String IM_INDEX_KEY = "imindex";
	private static final String TYPING_KEY = "typing";
	private static final String IM_KEY = "im";
	
	// these keys are used by peer info messages
	private static final String IP_KEY = "ip";
	private static final String PORT_KEY = "port";
	private static final String LOCAL_IP_KEY = "localip";
	private static final String LOCAL_PORT_KEY = "localport";
	private static final String STATUS_KEY = "status";
	private static final String SALT_KEY = "salt";
	
	private SessionId sid;
	private long messageType;
	
	private long messageIndex;
	private String message;
	private long typingVal;
	
	private InetSocketAddress netAddr;
	private InetSocketAddress localAddr;
	private long status;
	private String salt;
	
	
	public SessionId getSessionId() {
		return sid;
	}
	
	public void setSessionId(SessionId sid) {
		this.sid = sid;
	}
	
	public boolean isContentMessage() {
		return messageType == CONTENT_MESSAGE_TYPE;
	}
	
	public boolean isPeerInfoMessage() {
		return messageType == CLIENT_INFO_MESSAGE_TYPE;
	}
	
	public boolean isTypingMessage() {
		return messageType == USER_TYPING_MESSAGE_TYPE;
	}
	
	public boolean isAckMessage() {
		return messageType == ACK_MESSAGE_TYPE;
	}
	
	public long getMessageIndex() {
		return messageIndex;
	}
	
	public String getMessage() {
		return message;
	}
	
	public long getTypingVal() {
		return typingVal;
	}
	
	public InetSocketAddress getNetAddress() {
		return netAddr;
	}
	
	public InetSocketAddress getLocalAddress() {
		return localAddr;
	}
	
	public long getStatus() {
		return status;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public void setContentPayload(long messageIndex, String message) {
		this.messageType = CONTENT_MESSAGE_TYPE;
		this.messageIndex = messageIndex;
		this.message = message;
	}
	
	public void setTypingPayload(long messageIndex, long typingVal) {
		this.messageType = USER_TYPING_MESSAGE_TYPE;
		this.messageIndex = messageIndex;
		this.typingVal = typingVal;
	}
	
	public void setAcknowledgementPayload(long messageIndex) {
		this.messageType = ACK_MESSAGE_TYPE;
		this.messageIndex = messageIndex;
	}
	
	public void setClientInfoPayload(InetSocketAddress netAddr, InetSocketAddress localAddr, long status, String salt) {
		this.messageType = CLIENT_INFO_MESSAGE_TYPE;
		this.netAddr = netAddr;
		this.localAddr = localAddr;
		this.status = status;
		this.salt = salt;
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		sid = map.getAttributeValue(SID_KEY, new SessionIdAttributeValue());
		StringKeyedAttributeMap payload = map.getAttributeValue(PEER_MESSAGE_KEY, new StringKeyedMapAttributeValue());
		messageType = payload.getAttributeValue(MESSAGE_TYPE_KEY, new Int32AttributeValue());
		
		switch((int)messageType) {
		case CONTENT_MESSAGE_TYPE:
			messageIndex = payload.getAttributeValue(IM_INDEX_KEY, new Int32AttributeValue());
			message = payload.getAttributeValue(IM_KEY, new StringAttributeValue());
			break;
		case ACK_MESSAGE_TYPE:
			messageIndex = payload.getAttributeValue(IM_INDEX_KEY, new Int32AttributeValue());
			break;
		case CLIENT_INFO_MESSAGE_TYPE:
			netAddr = getAddress(payload, IP_KEY, PORT_KEY);
			localAddr = getAddress(payload, LOCAL_IP_KEY, LOCAL_PORT_KEY);
			status = payload.getAttributeValue(STATUS_KEY, new Int32AttributeValue());
			salt = payload.getAttributeValue(SALT_KEY, new StringAttributeValue());
			break;
		case USER_TYPING_MESSAGE_TYPE:
			messageIndex = payload.getAttributeValue(IM_INDEX_KEY, new Int32AttributeValue());
			typingVal = payload.getAttributeValue(TYPING_KEY, new Int32AttributeValue());
			break;
			default:
				Logging.messageLogger.warning("Unknown chat message type " + messageType + " found");
				Logging.messageLogger.finer("Unknown chat message contents:n" + map);
		}
	}

	private InetSocketAddress getAddress(StringKeyedAttributeMap map, String ipKey, String portKey) {
		long ip = map.getAttributeValue(ipKey, new Int32AttributeValue());
		long port = map.getAttributeValue(portKey, new Int32AttributeValue());
		return new InetSocketAddress(IOUtil.getAddressFromInt32(ip), (int)port);
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(SID_KEY, new SessionIdAttributeValue(sid));
		
		StringKeyedMapAttributeValue payload = new StringKeyedMapAttributeValue();
		StringKeyedAttributeMap payloadMap = payload.getValue();
		
		payloadMap.addAttribute(MESSAGE_TYPE_KEY, new Int32AttributeValue(messageType));
		
		switch((int)messageType) {
		case CONTENT_MESSAGE_TYPE:
			payloadMap.addAttribute(IM_INDEX_KEY, new Int32AttributeValue(messageIndex));
			payloadMap.addAttribute(IM_KEY, new StringAttributeValue(message));
			break;
		case ACK_MESSAGE_TYPE:
			payloadMap.addAttribute(IM_INDEX_KEY, new Int32AttributeValue(messageIndex));
			break;
		case CLIENT_INFO_MESSAGE_TYPE:
			long localIp = IOUtil.getInt32FromAddress((Inet4Address) localAddr.getAddress());
			long netIp = IOUtil.getInt32FromAddress((Inet4Address) netAddr.getAddress());
			payloadMap.addAttribute(IP_KEY, new Int32AttributeValue(netIp));
			payloadMap.addAttribute(PORT_KEY, netAddr.getPort());
			payloadMap.addAttribute(LOCAL_IP_KEY, new Int32AttributeValue(localIp));
			payloadMap.addAttribute(LOCAL_PORT_KEY, localAddr.getPort());
			payloadMap.addAttribute(STATUS_KEY, status);
			payloadMap.addAttribute(SALT_KEY, salt);
			break;
		case USER_TYPING_MESSAGE_TYPE:
			payloadMap.addAttribute(IM_INDEX_KEY, new Int32AttributeValue(messageIndex));
			payloadMap.addAttribute(TYPING_KEY, new Int32AttributeValue(typingVal));
			break;
		}
		
		map.addAttribute(PEER_MESSAGE_KEY, payload);
	}

	public int getMessageId() {
		return TYPE_ID;
	}

	public IMessage newInstance() {
		return new ChatMessage();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Chat Message\n");
		buffer.append("\tPeer SID: ");
		buffer.append(sid);
		buffer.append("\n\tType: ");
		switch((int)messageType) {
		case CONTENT_MESSAGE_TYPE:
			buffer.append("Content\n");
			buffer.append("\tMessage index: ");
			buffer.append(messageIndex);
			buffer.append("\n\tMessage: \"");
			buffer.append(message);
			buffer.append("\"");
			break;
		case ACK_MESSAGE_TYPE:
			buffer.append("Acknowledgement\n");
			buffer.append("\tMessage index: ");
			buffer.append(messageIndex);
			break;
		case CLIENT_INFO_MESSAGE_TYPE:
			buffer.append("Client info\n");
			buffer.append("\tNetwork address: ");
			buffer.append(netAddr);
			buffer.append("\n\tLocal address: ");
			buffer.append(localAddr);
			buffer.append("\n\tStatus: ");
			buffer.append(status);
			buffer.append("\n\tSalt: ");
			buffer.append(salt);
			break;
		case USER_TYPING_MESSAGE_TYPE:
			buffer.append("Typing\n");
			buffer.append("\tMessage index: ");
			buffer.append(messageIndex);
			buffer.append("\n\tTyping val: ");
			buffer.append(typingVal);
			break;
		default:
			buffer.append("Unknown\n");
		}
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ChatMessage) {
			ChatMessage cm = (ChatMessage)obj;
			
			return cm.messageType == this.messageType
			    && equalWithNull(cm.sid, this.sid)
			    && cm.messageIndex == this.messageIndex
			    && equalWithNull(cm.message, this.message)
			    && cm.typingVal == this.typingVal
			    && equalWithNull(cm.netAddr, this.netAddr)
			    && equalWithNull(cm.localAddr, this.localAddr)
			    && cm.status == this.status
			    && equalWithNull(cm.salt, this.salt);
		}
		
		return false;
	}
	
	private boolean equalWithNull(Object a, Object b) {
		if(a == null) {
			return b == null;
		}
		
		return a.equals(b);
	}
}
