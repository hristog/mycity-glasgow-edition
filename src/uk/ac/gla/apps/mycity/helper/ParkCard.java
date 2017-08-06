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

import uk.ac.gla.apps.mycity.R;

public class ParkCard {
	private int type;
	private int reference; // amount or a challenge/accessory/avatar ID
	
	public static final int MY_CITY_POINT_CARD = 0; // type
	
	public static final int MY_CITY_POINT_CARD_100 = 0;
	public static final int MY_CITY_POINT_CARD_250 = 1;
	public static final int MY_CITY_POINT_CARD_500 = 2;
	public static final int MY_CITY_POINT_CARD_1000 = 3;
	public static final int MY_CITY_POINT_CARD_2500 = 4;
	public static final int MY_CITY_POINT_CARD_5000 = 5;
	
	public static final int[] MYCITY_POINT_CARD_DRAWABLES = { R.drawable.collectible_mycity_points_100,
		R.drawable.collectible_mycity_points_250, R.drawable.collectible_mycity_points_500,
		R.drawable.collectible_mycity_points_1000, R.drawable.collectible_mycity_points_2500,
		R.drawable.collectible_mycity_points_5000 };
	
	public ParkCard(int type, int reference) {
		this.type = type;
		this.reference = reference;
	}
	
	public int getType() {
		return type;
	}
	
	public int getReference() {
		return reference;
	}
}