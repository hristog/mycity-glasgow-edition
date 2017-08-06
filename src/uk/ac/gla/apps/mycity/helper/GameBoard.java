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

import com.google.android.maps.GeoPoint;

public class GameBoard {
	private GeoPoint topLeft;
	private GeoPoint bottomRight;
	
	public GameBoard(GeoPoint topLeft, GeoPoint bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}

	public GeoPoint getTopLeft() {
		return topLeft;
	}

	public GeoPoint getBottomRight() {
		return bottomRight;
	}	
}