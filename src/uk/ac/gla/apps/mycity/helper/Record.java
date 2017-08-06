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

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable {
	private String displayName;
	private int overallScore;
	private int numCastles;
	private int numProperties;
	
	public Record(String displayName, int overallScore, int numCastles, int numProperties) {
		this.displayName = displayName;
		this.overallScore = overallScore;
		this.numCastles = numCastles;
		this.numProperties = numProperties;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public int getOverallScore() {
		return overallScore;
	}
	
	public int getNumCastles() {
		return numCastles;
	}
	
	public int getNumProperties() {
		return numProperties;
	}
	
	public Record(Parcel in) {
		displayName = in.readString();
		overallScore = in.readInt();
		numProperties = in.readInt();
		numCastles = in.readInt();
    }
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(displayName);
		out.writeInt(overallScore);
		out.writeInt(numProperties);
		out.writeInt(numCastles);
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}