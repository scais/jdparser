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

public class ApplicationInfo {

    private long id;
    private String longName;
    private String shortName;
    
    public ApplicationInfo(long id, String longName, String shortName) {
        this.id = id;
        this.longName = longName;
        this.shortName = shortName;
    }
    
    public long getId() {
        return id;
    }
    
    public String getLongName() {
        return longName;
    }
    
    public String getShortName() {
        return shortName;
    }
}
