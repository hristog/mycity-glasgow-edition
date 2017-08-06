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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Week implements Parcelable {
	private int week;
	private int year;
	
	public Week(int week, int year) {
		this.week = week;
		this.year = year;
	}
	
	
	public int getWeek() {
		return week;
	}

	/* public void setWeek(int week) {
		this.week = week;
	} */

	public int getYear() {
		return year;
	}

	/* public void setYear(int year) {
		this.year = year;
	} */

	public Week(Parcel in) {
		week = in.readInt();
		year = in.readInt();
    }
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(week);
		out.writeInt(year);
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static int[] getWeeks(List<Week> listOfWeeks) {
    	int i, numWeeks = listOfWeeks.size();
    	int[] weeks = new int[numWeeks];
    	for (i = 0; i < numWeeks; i++) {
    		weeks[i] = listOfWeeks.get(i).getWeek();
    	}
    	return weeks;
    }
	
	public static int[] getYears(List<Week> listOfWeeks) {
    	int i, numWeeks = listOfWeeks.size();
    	int[] years = new int[numWeeks];
    	for (i = 0; i < numWeeks; i++) {
    		years[i] = listOfWeeks.get(i).getYear();
    	}
    	return years;
    }
	
	public static String[] getCaptions(List<Week> listOfWeeks) {
    	int i, numWeeks = listOfWeeks.size();
    	String[] captions = new String[numWeeks];
    	for (i = 0; i < numWeeks; i++) {
    		captions[i] = listOfWeeks.get(i).getCaption();
    	}
    	return captions;
    }
	
	public String getCaption() {
		return String.format("%s - %s", firstDayOfWeek(getWeek(), getYear()), lastDayOfWeek(getWeek(), getYear()));
	}
	
	public static String firstDayOfWeek(int week, int year) {
	   Calendar calendar = Calendar.getInstance();
	   SimpleDateFormat dateFormatter = new SimpleDateFormat("E, MMM d");
	   calendar.clear();
	   calendar.set(Calendar.WEEK_OF_YEAR, week);
	   calendar.set(Calendar.YEAR, year);
	   return dateFormatter.format(calendar.getTime());
    }
   
    public static String lastDayOfWeek(int week, int year) {
	   Calendar calendar = Calendar.getInstance();
	   SimpleDateFormat dateFormatter = new SimpleDateFormat("E, MMM d");
	   calendar.clear();
	   calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
	   calendar.set(Calendar.WEEK_OF_YEAR, week);
	   calendar.set(Calendar.YEAR, year);
	   return dateFormatter.format(calendar.getTime());
    }
}