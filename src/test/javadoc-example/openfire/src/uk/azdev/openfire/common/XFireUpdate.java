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

public class XFireUpdate {

	private long versionNum;
	private String updateDownloadUrl;
	private long command;
	private long fileId;
	
	public XFireUpdate(long versionNum, String updateDownloadUrl, long command, long fileId) {
		this.versionNum = versionNum;
		this.updateDownloadUrl = updateDownloadUrl;
		this.command = command;
		this.fileId = fileId;
	}
	
	public long getVersionNum() {
		return versionNum;
	}
	
	public String getUpdateDownloadUrl() {
		return updateDownloadUrl;
	}
	
	public long getCommand() {
		return command;
	}
	
	public long getFileId() {
		return fileId;
	}
}
