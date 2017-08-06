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

import uk.ac.gla.apps.mycity.R;

public class Collectible {
	private int id;
	private int xCoord;
	private int yCoord;
	private int xGrid;
	private int yGrid;
	private String name;
	private int type;
	private String description;
	private int drawableId;
	private int quantity;
	
	public final static int PARK_CARD = 1;
	public final static int CITY_CARD = 2;
	public final static int CUSTOMISATION_CARD = 3;
	
	public final static int[] CARD_DRAWABLE_IDS = { 0, R.drawable.park_card_default, R.drawable.city_card_default, R.drawable.customisation_card_default };
	
	public Collectible(int id, int xCoord, int yCoord, int xGrid, int yGrid,
			String name, int type, String description, int drawableId, int quantity) {
		this.id = id;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.xGrid = xGrid;
		this.yGrid = yGrid;
		this.name = name;
		this.type = type;
		this.description = description;
		this.drawableId = drawableId;
		this.quantity = quantity;
	}

	public static int[] getDrawableIds (List<Collectible> listOfCollectibles) {
    	int i, numCollectibles = listOfCollectibles.size();
    	int[] collectibleDrawableIds = new int[numCollectibles];
    	for (i = 0; i < numCollectibles; i++) {
    		collectibleDrawableIds[i] = listOfCollectibles.get(i).getDrawableId();
    	}
    	return collectibleDrawableIds;
    }
	
	public static String[] getNames(List<Collectible> listOfCollectibles) {
    	int i, numCollectibles = listOfCollectibles.size();
    	String[] collectibleNames = new String[numCollectibles];
    	for (i = 0; i < numCollectibles; i++) {
    		collectibleNames[i] = listOfCollectibles.get(i).getName();
    	}
    	return collectibleNames;
    }
	
	public static String[] getDescriptions(List<Collectible> listOfCollectibles) {
    	int i, numCollectibles = listOfCollectibles.size();
    	String[] collectibleDescriptions = new String[numCollectibles];
    	for (i = 0; i < numCollectibles; i++) {
    		collectibleDescriptions[i] = listOfCollectibles.get(i).getDescription();
    	}
    	return collectibleDescriptions;
    }
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getDrawableId() {
		return drawableId;
	}
	
	/* TODO: remove if unnecessary
	 * public Collectible(int id) {
		this.id = id;
		this.xCoord = 0;
		this.yCoord = 0;
		this.xGrid = 0;
		this.yGrid = 0;
		this.name = "";
		this.type = 0;
		this.description = "";
		this.drawableId = 0;
		this.quantity = 0;
	} */
}