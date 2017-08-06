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
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
	private int id;
	private int tokenId;
	private int houseType;
	private int myCityPoints;
	private int overallScore;
	private int numProperties;
	private int numCastles;
	private String displayName;
	private String email;
	
	public User(int id, int tokenId, int houseType, int myCityPoints, int overallScore, int numProperties, int numCastles, String displayName, String email) {
		this.id = id;
		this.tokenId = tokenId;
		this.houseType = houseType;
		this.myCityPoints = myCityPoints;
		this.overallScore = overallScore;
		this.numProperties = numProperties;
		this.numCastles = numCastles;
		this.displayName = displayName;
		this.email = email;
	}
	
	private void defaultSetUp() {
		id = -1;
		tokenId = R.drawable.token_default;
		houseType = R.drawable.house_default;
		Log.v("HOUSE_TYPE", String.format("%d", houseType));
		numProperties = numCastles = 0;
		overallScore = 0; // TODO: 500 per house
		myCityPoints = 0; // TODO: 100 per mile
	}
	
	public User(int id, String displayName, String email) {
		defaultSetUp();
		this.id = id;
		this.displayName = displayName;
		this.email = email;
	}
	
	public User(int id, String displayName, String email, int tokenId, int houseType) {
		this(id, displayName, email);
		this.tokenId = tokenId;
		this.houseType = houseType;
	}
	
	public User() {
		defaultSetUp();
		displayName = "Anonymous User";
		email = "anonymous@user.com";
	}
	
	public User(int tokenId) {
		this();
		this.tokenId = tokenId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getMyCityPoints() {
		return myCityPoints;
	}
	
	public int getOverallScore() {
		return overallScore;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public int getTokenId() {
		return tokenId;
	}
	
	public int getHouseType() {
		return houseType;
	}

	public int describeContents() {
		return 0;
	}
	
	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}
	
	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}
	
	public void setMyCityPoints(int myCityPoints) {
		this.myCityPoints = myCityPoints;
	}
	
	private User(Parcel in) {
		id = in.readInt();
		tokenId = in.readInt();
		houseType = in.readInt();
		myCityPoints = in.readInt();
		overallScore = in.readInt();
		numProperties = in.readInt();
		numCastles = in.readInt();
		displayName = in.readString();
		email = in.readString();
    }

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeInt(tokenId);
		out.writeInt(houseType);
		out.writeInt(myCityPoints);
		out.writeInt(overallScore);
		out.writeInt(numProperties);
		out.writeInt(numCastles);
		out.writeString(displayName);
		out.writeString(email);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    
    public static int[] getIds(List<User> listOfUsers) {
    	int i, numUsers = listOfUsers.size();
    	int[] userIds = new int[numUsers];
    	for (i = 0; i < numUsers; i++) {
    		userIds[i] = listOfUsers.get(i).getId();
    	}
    	return userIds;
    }
    
    public static String[] getNames(List<User> listOfUsers) {
    	int i, numUsers = listOfUsers.size();
    	String[] userNames = new String[numUsers];
    	for (i = 0; i < numUsers; i++) {
    		userNames[i] = listOfUsers.get(i).getDisplayName();
    	}
    	return userNames;
    }
    
    public static int[] getTokenIds(List<User> listOfUsers) {
    	int i, numUsers = listOfUsers.size();
    	int[] userTokenIds = new int[numUsers];
    	for (i = 0; i < numUsers; i++) {
    		userTokenIds[i] = listOfUsers.get(i).getTokenId();
    	}
    	return userTokenIds;
    }
    
    public void increaseMyCityPoints(int amount) {
    	myCityPoints += amount;
    }
    
    public void decreaseMyCityPoints(int amount) {
    	myCityPoints -= amount;
    }
    
    public void increaseOverallScore(int amount) {
    	overallScore += amount;
    }
    
    public void decreaseOverallScore(int amount) {
    	overallScore -= amount;
    }
    
    public static int[] getTokenIds(int numUsers) {
    	int i;
    	int[] userTokenIds = new int[numUsers];
    	for (i = 0; i < numUsers; i++) {
    		userTokenIds[i] = R.drawable.token_unknown;
    	}
    	return userTokenIds;
    }
}