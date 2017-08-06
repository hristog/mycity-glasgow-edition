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

public class Activity {
	private int userId;
	private int week;
	private int year;
	private int type;
	private int duration;
	
	public final static int numActivityTypes = 5; // 4 activity types (w/o counting sedentary) + recommended
	public final static int RECOMMENDED = 0; // x 1
	public final static int WALKING = 1; // x 1
	public final static int RUNNING = 2; // x 3
	public final static int CYCLING = 3; // x 2
	public final static int SWIMMING = 4; // x 2.5
	public final static int SEDENTARY = 5; // x 0
	public final static int RECOMMENDED_DURATION = 150; // mins
	public static final int[] COEFFICIENTS = { 10, 10, 20, 15, 15 };
	public static final String[] CAPTIONS = { "Remaining", "Walking", "Running",
										      "Cycling", "Swimming" };
	
	public Activity(int userId, int week, int year, int type, int duration) {
		this.userId = userId;
		this.week = week;
		this.year = year;
		this.type = type;
		this.duration = duration;
	}
	
	public int getType() {
		return type;
	}
	
	public int getDuration() {
		return duration;
	}
}