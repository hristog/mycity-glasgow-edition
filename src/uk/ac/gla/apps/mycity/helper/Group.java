/**
 * MyCity - An app encouraging physical activity in a fun way
 * 
 * @author Hristo Georgiev, 1003619
 * School of Computing Science, University of Glasgow
 * Aug, 2012
 * ver. 1.0
 * 
 * Copyright (C) 2012 Hristo Georgiev

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.gla.apps.mycity.helper;

import java.util.List;

public class Group {
	private int id;
	private String name;
	private String description;
	
	public Group(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public static String[] getNames(List<Group> listOfGroups) {
    	int i, numGroups = listOfGroups.size();
    	String[] groupNames = new String[numGroups];
    	for (i = 0; i < numGroups; i++) {
    		groupNames[i] = listOfGroups.get(i).getName();
    	}
    	return groupNames;
    }
	
	public static String[] getDescriptions(List<Group> listOfGroups) {
    	int i, numGroups = listOfGroups.size();
    	String[] groupDescriptions = new String[numGroups];
    	for (i = 0; i < numGroups; i++) {
    		groupDescriptions[i] = listOfGroups.get(i).getDescription();
    	}
    	return groupDescriptions;
    }
	
	public static int[] getIds(List<Group> listOfGroups) {
    	int i, numGroups = listOfGroups.size();
    	int[] groupIds = new int[numGroups];
    	for (i = 0; i < numGroups; i++) {
    		groupIds[i] = listOfGroups.get(i).getId();
    	}
    	return groupIds;
    }
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}