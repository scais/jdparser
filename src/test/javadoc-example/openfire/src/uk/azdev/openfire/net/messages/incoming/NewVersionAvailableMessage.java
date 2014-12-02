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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.common.XFireUpdate;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class NewVersionAvailableMessage extends StringMapBasedMessage {

	public static final int TYPE_ID = 134;
	
	public static final String NEW_VERSION_NUMS_KEY = "version";
	public static final String NEW_VERSION_FILES_KEY = "file";
	public static final String COMMAND_KEY = "command";
	public static final String FILE_ID_KEY = "fileid";
	public static final String FLAGS_KEY = "flags";
	
	private List<XFireUpdate> newVersionsList;
	private long flags;
	
	public NewVersionAvailableMessage() {
		newVersionsList = new LinkedList<XFireUpdate>();
		flags = 0;
	}
	
	public int getMessageId() {
		return TYPE_ID;
	}

	public IMessage newInstance() {
		return new NewVersionAvailableMessage();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		Iterator<Long> newVersionNumsIter = map.getAttributeValueAsList(NEW_VERSION_NUMS_KEY, new Int32AttributeValue()).iterator();
		Iterator<String> newVersionUrlsIter = map.getAttributeValueAsList(NEW_VERSION_FILES_KEY, new StringAttributeValue()).iterator();
		Iterator<Long> commandsIter = map.getAttributeValueAsList(COMMAND_KEY, new Int32AttributeValue()).iterator();
		Iterator<Long> fileIdsIter = map.getAttributeValueAsList(FILE_ID_KEY, new Int32AttributeValue()).iterator();
		
		while(newVersionNumsIter.hasNext()) {
			XFireUpdate update = new XFireUpdate(newVersionNumsIter.next(), newVersionUrlsIter.next(), commandsIter.next(), fileIdsIter.next());
			newVersionsList.add(update);
		}
		
		flags = map.getAttributeValue(FLAGS_KEY, new Int32AttributeValue());
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue newVersionNums = new ListAttributeValue();
		ListAttributeValue newVersionUrls =  new ListAttributeValue();
		ListAttributeValue commands =  new ListAttributeValue();
		ListAttributeValue fileIds =  new ListAttributeValue();
		
		for(XFireUpdate update : newVersionsList) {
			newVersionNums.addValue(new Int32AttributeValue(update.getVersionNum()));
			newVersionUrls.addValue(new StringAttributeValue(update.getUpdateDownloadUrl()));
			commands.addValue(new Int32AttributeValue(update.getCommand()));
			fileIds.addValue(new Int32AttributeValue(update.getFileId()));
		}
		
		map.addAttribute(NEW_VERSION_NUMS_KEY, newVersionNums);
		map.addAttribute(NEW_VERSION_FILES_KEY, newVersionUrls);
		map.addAttribute(COMMAND_KEY, commands);
		map.addAttribute(FILE_ID_KEY, fileIds);
		map.addAttribute(FLAGS_KEY, new Int32AttributeValue(flags));
	}

	public List<XFireUpdate> getNewVersionsList() {
		return newVersionsList;
	}
	
	public void addNewVersion(XFireUpdate update) {
		newVersionsList.add(update);
	}
	
	public long getFlags() {
		return flags;
	}
	
	public void setFlags(long flags) {
		this.flags = flags;
	}
}
