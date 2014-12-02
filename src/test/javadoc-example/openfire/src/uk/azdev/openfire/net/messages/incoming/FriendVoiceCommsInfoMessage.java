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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import uk.azdev.openfire.common.ActiveApplicationInfo;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class FriendVoiceCommsInfoMessage extends StringMapBasedMessage {

    private static final String SESSION_ID_LIST_KEY = "sid";
    private static final String COMMS_SERVER_TYPE_ID_LIST = "vid";
    private static final String COMMS_SERVER_IP_LIST = "vip";
    
    public static final int MESSAGE_ID = 147;
    
    private LinkedHashMap<SessionId, ActiveApplicationInfo> sidToAppInfo;
    
    public FriendVoiceCommsInfoMessage() {
        sidToAppInfo = new LinkedHashMap<SessionId, ActiveApplicationInfo>();
    }
    
    @Override
    protected void interpretAttributeMap(StringKeyedAttributeMap map) {
        Iterator<SessionId> sidIter = map.getAttributeValueAsList(SESSION_ID_LIST_KEY, new SessionIdAttributeValue()).iterator();
        Iterator<Long> typeIdIter = map.getAttributeValueAsList(COMMS_SERVER_TYPE_ID_LIST, new Int32AttributeValue()).iterator();
        Iterator<Long> ipIter = map.getAttributeValueAsList(COMMS_SERVER_IP_LIST, new Int32AttributeValue()).iterator();
        
        while(sidIter.hasNext()) {
            SessionId sid = sidIter.next();
            long typeId = typeIdIter.next();
            long ipAsInt = ipIter.next();
            
            Inet4Address addr;
            if(ipAsInt == 0) {
                addr = null;
            } else {
                addr = IOUtil.getAddressFromInt32(ipAsInt);
            }
            
            sidToAppInfo.put(sid, new ActiveApplicationInfo(typeId, addr));
        }
    }

    @Override
    protected void populateAttributeMap(StringKeyedAttributeMap map) {
        ListAttributeValue sidList = new ListAttributeValue();
        ListAttributeValue appTypeIdList = new ListAttributeValue();
        ListAttributeValue ipList = new ListAttributeValue();
        
        for(Entry<SessionId, ActiveApplicationInfo> entry : sidToAppInfo.entrySet()) {
            sidList.addValue(new SessionIdAttributeValue(entry.getKey()));
            appTypeIdList.addValue(new Int32AttributeValue(entry.getValue().getAppId()));
            ipList.addValue(new Int32AttributeValue(entry.getValue().getAddress()));
        }
        
        map.addAttribute(SESSION_ID_LIST_KEY, sidList);
        map.addAttribute(COMMS_SERVER_TYPE_ID_LIST, appTypeIdList);
        map.addAttribute(COMMS_SERVER_IP_LIST, ipList);
    }

    public int getMessageId() {
        return MESSAGE_ID;
    }

    public IMessage newInstance() {
        return new FriendVoiceCommsInfoMessage();
    }

    public Set<SessionId> getSessionIdSet() {
        return sidToAppInfo.keySet();
    }
    
    public ActiveApplicationInfo getActiveAppForSid(SessionId sid) {
        return sidToAppInfo.get(sid);
    }
    
    public void addActiveAppInfo(SessionId sid, ActiveApplicationInfo info) {
        sidToAppInfo.put(sid, info);
    }

}
