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

package uk.ac.gla.apps.mycity.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomOverlayItem extends OverlayItem {
	private long id;
	private int x_grid;
	private int y_grid;
	private int type;
	private int quantity;
	
	public CustomOverlayItem(GeoPoint point, String title, String snippet, long id, int x_grid, int y_grid, int type, int quantity) {
		super(point, title, snippet);
		this.id = id;
		this.x_grid = x_grid;
		this.y_grid = y_grid;
		this.type = type;
		this.quantity = quantity;
	}
	
	public CustomOverlayItem(GeoPoint point, String title, String snippet, long id, int x_grid, int y_grid, int type) {
		this(point, title, snippet, id, x_grid, y_grid, type, 1);
	}
	
	public long getId() {
		return id;
	}
	
	public int getXGrid() {
		return x_grid;
	}
	
	public int getYGrid() {
		return y_grid;
	}
	
	public int getType() {
		return type;
	}
	
	public int getQuantity() {
		return quantity;
	}
}