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

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class LoginSuccessMessage extends StringMapBasedMessage {

	private static final String PUBLIC_IP_KEY = "pip";
	private static final String N3_KEY = "n3";
	private static final String N2_KEY = "n2";
	private static final String N1_KEY = "n1";
	private static final String CTRY_KEY = "ctry";
	private static final String MAX_RECT_KEY = "maxrect";
	private static final String MIN_RECT_KEY = "minrect";
	private static final String CLNTSET_KEY = "clntset";
	private static final String P2PSET_KEY = "p2pset";
	private static final String DLSET_KEY = "dlset";
	private static final String STATUS_KEY = "status";
	private static final String NICK_KEY = "nick";
	private static final String SESSION_ID_KEY = "sid";
	private static final String USER_ID_KEY = "userid";

	public static final int LOGIN_SUCCESS_MESSAGE_ID = 130;

	private long userId;
	private SessionId sessionId;
	private String nick;
	private long status;
	private String dlSet;
	private String p2pSet;
	private String clntSet;
	private long minRect;
	private long maxRect;
	private long ctry;
	private Inet4Address n1;
	private Inet4Address n2;
	private Inet4Address n3;
	private Inet4Address publicIp;
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		userId = map.getInt32AttributeValue(USER_ID_KEY);
		sessionId = ((SessionIdAttributeValue)map.getAttributeValue(SESSION_ID_KEY)).getValue();
		nick = map.getStringAttributeValue(NICK_KEY);
		status = map.getInt32AttributeValue(STATUS_KEY);
		dlSet = map.getStringAttributeValue(DLSET_KEY);
		p2pSet = map.getStringAttributeValue(P2PSET_KEY);
		clntSet = map.getStringAttributeValue(CLNTSET_KEY);
		minRect = map.getInt32AttributeValue(MIN_RECT_KEY);
		maxRect = map.getInt32AttributeValue(MAX_RECT_KEY);
		ctry = map.getInt32AttributeValue(CTRY_KEY);
		n1 = ((Int32AttributeValue)map.getAttributeValue(N1_KEY)).getValueAsInetAddress();
		n2 = ((Int32AttributeValue)map.getAttributeValue(N2_KEY)).getValueAsInetAddress();
		n3 = ((Int32AttributeValue)map.getAttributeValue(N3_KEY)).getValueAsInetAddress();
		publicIp = ((Int32AttributeValue)map.getAttributeValue(PUBLIC_IP_KEY)).getValueAsInetAddress();
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(USER_ID_KEY, userId);
		map.addAttribute(SESSION_ID_KEY, new SessionIdAttributeValue(sessionId));
		map.addAttribute(NICK_KEY, nick);
		map.addAttribute(STATUS_KEY, status);
		map.addAttribute(DLSET_KEY, dlSet);
		map.addAttribute(P2PSET_KEY, p2pSet);
		map.addAttribute(CLNTSET_KEY, clntSet);
		map.addAttribute(MIN_RECT_KEY, minRect);
		map.addAttribute(MAX_RECT_KEY, maxRect);
		map.addAttribute(CTRY_KEY, ctry);
		map.addAttribute(N1_KEY, new Int32AttributeValue(n1));
		map.addAttribute(N2_KEY, new Int32AttributeValue(n2));
		map.addAttribute(N3_KEY, new Int32AttributeValue(n3));
		map.addAttribute(PUBLIC_IP_KEY, new Int32AttributeValue(publicIp));
	}

	public int getMessageId() {
		return LOGIN_SUCCESS_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new LoginSuccessMessage();
	}

	public String getClntSet() {
		return clntSet;
	}

	public void setClntSet(String clntSet) {
		this.clntSet = clntSet;
	}

	public long getCtry() {
		return ctry;
	}

	public void setCtry(long ctry) {
		this.ctry = ctry;
	}

	public String getDlSet() {
		return dlSet;
	}

	public void setDlSet(String dlSet) {
		this.dlSet = dlSet;
	}

	public long getMaxRect() {
		return maxRect;
	}

	public void setMaxRect(long maxRect) {
		this.maxRect = maxRect;
	}

	public long getMinRect() {
		return minRect;
	}

	public void setMinRect(long minRect) {
		this.minRect = minRect;
	}

	public Inet4Address getN1() {
		return n1;
	}

	public void setN1(Inet4Address n1) {
		this.n1 = n1;
	}

	public Inet4Address getN2() {
		return n2;
	}

	public void setN2(Inet4Address n2) {
		this.n2 = n2;
	}

	public Inet4Address getN3() {
		return n3;
	}

	public void setN3(Inet4Address n3) {
		this.n3 = n3;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getP2pSet() {
		return p2pSet;
	}

	public void setP2pSet(String set) {
		p2pSet = set;
	}

	public Inet4Address getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(Inet4Address peerIp) {
		this.publicIp = peerIp;
	}

	public SessionId getSessionId() {
		return sessionId;
	}

	public void setSessionId(SessionId sessionId) {
		this.sessionId = sessionId;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Login Success Message");
		buffer.append("\n\tUser id: ");
		buffer.append(userId);
		buffer.append("\n\tSession id: ");
		buffer.append(sessionId.toString());
		buffer.append("\n\tNick: ");
		buffer.append(nick);
		buffer.append("\n\tStatus: ");
		buffer.append(status);
		buffer.append("\n\tDlset: ");
		buffer.append(dlSet);
		buffer.append("\n\tP2P Set: ");
		buffer.append(p2pSet);
		buffer.append("\n\tClnt Set: ");
		buffer.append(clntSet);
		buffer.append("\n\tMin rect: ");
		buffer.append(minRect);
		buffer.append("\n\tMax rect: ");
		buffer.append(maxRect);
		buffer.append("\n\tCtry: ");
		buffer.append(ctry);
		buffer.append("\n\tN1: ");
		buffer.append(n1.toString());
		buffer.append("\n\tN2: ");
		buffer.append(n2.toString());
		buffer.append("\n\tN3: ");
		buffer.append(n3.toString());
		buffer.append("\n\tPublic IP: ");
		buffer.append(publicIp.toString());
		
		return buffer.toString();
	}
}
