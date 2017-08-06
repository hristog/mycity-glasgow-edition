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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.map.CustomItemizedOverlay;
import uk.ac.gla.apps.mycity.map.CustomOverlayItem;
import uk.ac.gla.apps.mycity.map.MapHelper;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private class Table {
		private HashMap<String, Integer> attributes;
		private String name;
		private int lastIndex;
		
		private Table(String name) {
			this.name = name;
			lastIndex = 0;
			attributes = new HashMap<String, Integer>();
		}
		
		private void addAttribute(String name) {
			attributes.put(name, lastIndex++);
		}
		
		private int getIndex(String name) {
			return attributes.get(name);
		}
	}
	
	private static final int DATABASE_VERSION = 1;
	private static final String DB_PATH = "/data/data/uk.ac.gla.apps.mycity/databases/";
	private static final String DB_NAME = "myCity.sqlite";
	private SQLiteDatabase myDatabase;
	private final Context myContext;
	
	private static Random generator;
	private static Calendar calendar;

	private Table gameBoard;
	private Table users;
	private Table activity;
	private Table properties;
	private Table friends;
	private Table requests;
	// private Table groups;
	// private Table usersInGroups;
	private Table collectibles;
	private Table collectedItems;
	private Table challenges;
	private Table challengesCompleted;
	
	private static final int FRIENDSHIP_REQUEST = 0;
	// private static final int CHALLENGE_REQUEST = 1;
	// private static final int GROUP_REQUEST = 2;
	
	private static final int NUM_CHANCE_CARD_ITEM_TYPES = 1; // only 0
	private static final int MYCITY_POINTS = 0;
	// private static final int UNLOCKED_AVATAR = 1;
	// private static final int UNLOCKED_ACCESSORY = 2;
	// private static final int UNLOCKED_CHALLENGE = 3;
	
	private static final int NOT_USED_YET = 0; // for collectibles
			
	private static final int[] MONEY_GIVEAWAY_AMOUNTS = { 100, 250, 500, 1000, 2500, 5000 };

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		
		calendar = Calendar.getInstance();
		generator = new Random(calendar.getTimeInMillis());
		
		// the order in which the attributes are added, determines the actual consecutive number of each of them
		gameBoard = new Table("GameBoard");
		gameBoard.addAttribute("edition_name");
		gameBoard.addAttribute("top_left_x_coord");
		gameBoard.addAttribute("top_left_y_coord");
		gameBoard.addAttribute("bottom_right_x_coord");
		gameBoard.addAttribute("bottom_right_y_coord");
		
		users = new Table("Users");
		users.addAttribute("id");
		users.addAttribute("token_id");
		users.addAttribute("house_type");
		users.addAttribute("mycity_points");
		users.addAttribute("overall_score");
		users.addAttribute("num_properties");
		users.addAttribute("num_castles");
		users.addAttribute("name");
		users.addAttribute("email");
		users.addAttribute("pass");
		
		activity = new Table("Activity");
		activity.addAttribute("user_id");
		activity.addAttribute("week");
		activity.addAttribute("year");
		activity.addAttribute("type");
		activity.addAttribute("duration");
		
		properties = new Table("Properties");
		properties.addAttribute("id");
		properties.addAttribute("x_coord");
		properties.addAttribute("y_coord");
		properties.addAttribute("x_grid");
		properties.addAttribute("y_grid");
		properties.addAttribute("title");
		properties.addAttribute("drawable_id");
		properties.addAttribute("description");
		properties.addAttribute("owner_id");
		
		friends = new Table("Friends");
		friends.addAttribute("user1_id");
		friends.addAttribute("user2_id");
		
		requests = new Table("Requests");
		requests.addAttribute("from_user_id");
		requests.addAttribute("to_user_id");
		requests.addAttribute("type");
		requests.addAttribute("reference_id");
		requests.addAttribute("response");
				
		/* groups = new Table("Groups");
		groups.addAttribute("id");
		groups.addAttribute("name");
		groups.addAttribute("description");
					
		usersInGroups = new Table("usersInGroups");
		usersInGroups.addAttribute("group_id");
		usersInGroups.addAttribute("user_id"); */
		
		collectibles = new Table("Collectibles");
		collectibles.addAttribute("id");
		collectibles.addAttribute("x_coord");
		collectibles.addAttribute("y_coord");
		collectibles.addAttribute("x_grid");
		collectibles.addAttribute("y_grid");
		collectibles.addAttribute("name");
		collectibles.addAttribute("type");
		collectibles.addAttribute("description");
		collectibles.addAttribute("drawable_id");
		collectibles.addAttribute("quantity");
		
		collectedItems = new Table("CollectedItems");
		collectedItems.addAttribute("user_id");
		collectedItems.addAttribute("collectible_id");
		collectedItems.addAttribute("instances_owned");
		collectedItems.addAttribute("renewable_after_day");
		collectedItems.addAttribute("renewable_after_year");
		
		challenges = new Table("Challenges");
		challenges.addAttribute("id");
		challenges.addAttribute("name");
		challenges.addAttribute("type");
		challenges.addAttribute("description");
		
		challengesCompleted = new Table("ChallengesCompleted");
		challengesCompleted.addAttribute("challenge_id");
		challengesCompleted.addAttribute("user_id");
		
		try {
			this.createDataBase();
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}

		try {
			this.openDataBase();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean databaseExists() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,	SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			return false;
		}

		checkDB.close();
		return true;
	}
	
	public void createDataBase() throws IOException {
		if (databaseExists()) {
			Log.v("DB Exists", "db exists");
			this.getWritableDatabase();
		}
		else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.v("Database Upgrade", "Database version higher than old.");
		}
		myContext.deleteDatabase(DB_NAME);
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public void close() {
		myDatabase.close();
	}
	
	/********************************* General  *********************************/
	
	public static int getRandomInt(int maxVal) {
		return getRandomInt(1, maxVal);
	}
	
	public static int getRandomInt(int minVal, int maxVal) {
		return minVal + generator.nextInt(maxVal);
	}
	
	public GameBoard getGameBoard(String editionName) {
		GameBoard gameBoardGlasgowEdition;
		String gameBoardByEditionQuery = String.format("SELECT * FROM %s WHERE edition_name = '%s'", gameBoard.name, editionName);
		Cursor cursor = myDatabase.rawQuery(gameBoardByEditionQuery, null);
		
		if (cursor.moveToFirst()) {
			gameBoardGlasgowEdition = new GameBoard(new GeoPoint(cursor.getInt(gameBoard.getIndex("top_left_x_coord")), cursor.getInt(gameBoard.getIndex("top_left_y_coord"))), new GeoPoint(cursor.getInt(gameBoard.getIndex("bottom_right_x_coord")), cursor.getInt(gameBoard.getIndex("bottom_right_y_coord"))));
		}
		else {
			gameBoardGlasgowEdition = null;
		}
		cursor.close();
		return gameBoardGlasgowEdition;
	}
		
	/********************************* Activity [1] *********************************/
	
	public boolean logActivity(int userId, int week, int year, int type, int duration) {
		ContentValues values = new ContentValues();
		values.put("user_id", userId);
		values.put("week", week);
		values.put("year", year);
		values.put("type", type);
		values.put("duration", duration);
		
		return myDatabase.insert(activity.name, null, values) != -1;
	}
	
	public List<Activity> getOverallActivity(int userId) {
		List<Activity> weeklyActivity = new ArrayList<Activity>();
		String weeklyActivityQuery = String.format("SELECT * FROM %s WHERE user_id = %d", activity.name, userId);
		Cursor cursor = myDatabase.rawQuery(weeklyActivityQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				weeklyActivity.add(new Activity(cursor.getInt(activity.getIndex("user_id")), cursor.getInt(activity.getIndex("week")), 
					cursor.getInt(activity.getIndex("year")), cursor.getInt(activity.getIndex("type")), cursor.getInt(activity.getIndex("duration"))));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return weeklyActivity;
	}
	
	public List<Activity> getWeeklyActivity(int userId, int week, int year) {
		List<Activity> weeklyActivity = new ArrayList<Activity>();
		String weeklyActivityQuery = String.format("SELECT * FROM %s WHERE user_id = %d AND week = %d AND year = %d", activity.name, userId, week, year);
		Cursor cursor = myDatabase.rawQuery(weeklyActivityQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				weeklyActivity.add(new Activity(cursor.getInt(activity.getIndex("user_id")), cursor.getInt(activity.getIndex("week")), 
					cursor.getInt(activity.getIndex("year")), cursor.getInt(activity.getIndex("type")), cursor.getInt(activity.getIndex("duration"))));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return weeklyActivity;
	}
	
	public List<Week> getWeekListByUser(int userId) {
		List<Week> weekList = new ArrayList<Week>();
		String weekListQuery = String.format("SELECT week, year FROM %s WHERE user_id = %d GROUP BY week, year", activity.name, userId);
		Cursor cursor = myDatabase.rawQuery(weekListQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				weekList.add(new Week(cursor.getInt(0), cursor.getInt(1)));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return weekList;
	}
	
	public int calculateMyCityPoints(List<Activity> listOfActivities) {
		int totalMyCityPoints = 0;
		for (Activity a : listOfActivities) {
			totalMyCityPoints += a.getDuration() * Activity.COEFFICIENTS[a.getType()];
		}
		return totalMyCityPoints;
	}
	
	/********************************* Profile/Account [2] *********************************/
	
	public int addUser(String displayName, String email, String pass, int tokenId, int houseType) {
		if (!userExists(email)) {
			ContentValues values = new ContentValues();
			values.put("name", displayName);
			values.put("email", email);
			try {
				values.put("pass", SecurityManager.encrypt(pass));
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			values.put("token_id", tokenId);
			values.put("house_type", houseType);
			
			// Inserting Row
			return (int) myDatabase.insert(users.name, null, values);
		}
		return -1;
	}
	
	public User logIn(String email, String pass) {
		String encryptedPass = null;
		User user = null;
		try {
			encryptedPass = SecurityManager.encrypt(pass);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		encryptedPass = encryptedPass.substring(0, encryptedPass.length()-1); // TODO: test with different combinations of user names and passwords
		Log.i("PASS", String.format("[%s] [%s]", pass, encryptedPass));
		
		String checkUser = String.format("SELECT * FROM %s WHERE email = '%s' AND pass = '%s'", users.name, email, encryptedPass);
		Cursor cursor = myDatabase.rawQuery(checkUser, null);
		if (cursor.moveToFirst()) {
			user = new User(cursor.getInt(users.getIndex("id")), cursor.getInt(users.getIndex("token_id")),	cursor.getInt(users.getIndex("house_type")), cursor.getInt(users.getIndex("mycity_points")),
				cursor.getInt(users.getIndex("overall_score")), cursor.getInt(users.getIndex("num_properties")), cursor.getInt(users.getIndex("num_castles")),
				cursor.getString(users.getIndex("name")), cursor.getString(users.getIndex("email")));
		}
		cursor.close();
		return user;
	}
		
	public boolean userExists(String email) {
		String checkUser = String.format("SELECT email FROM %s WHERE email = '%s'", users.name, email);
		Cursor cursor = myDatabase.rawQuery(checkUser, null);
		return cursor.moveToFirst();
	}
	
	public boolean updateUserToken(int userId, int tokenId) {
		ContentValues values = new ContentValues();
		values.put("token_id", tokenId);

		return myDatabase.update(users.name, values,
				"id = ?",
				new String[] { String.valueOf(userId) }) == 1;		
	}
	
	public boolean updateUserHouseType(int userId, int houseType) {
		ContentValues values = new ContentValues();
		values.put("house_type", houseType);

		return myDatabase.update(users.name, values,
				"id = ?",
				new String[] { String.valueOf(userId) }) == 1;		
	}
	
	/********************************* Friends [3] *********************************/
	
	public int getUserCount() {
		String getCountQuery = String.format("SELECT * FROM Users");
		Cursor cursor = myDatabase.rawQuery(getCountQuery, null);
		return cursor.getCount();
	}
	
	public List<User> getAllFriends(int userId) {
		List<User> friendList = new ArrayList<User>();
		String allFriendsQuery = String.format("SELECT * FROM Users");
		// String allFriendsQuery = String.format("SELECT * FROM Users, Friends WHERE Friends.user1_id = %d AND Users.id = Friends.user2_id", userId);
		Cursor cursor = myDatabase.rawQuery(allFriendsQuery, null);
	
		if (cursor.moveToFirst()) {
			do {
				friendList.add(new User(cursor.getInt(users.getIndex("id")), cursor.getInt(users.getIndex("token_id")), cursor.getInt(users.getIndex("house_type")), cursor.getInt(users.getIndex("mycity_points")),
					cursor.getInt(users.getIndex("overall_score")), cursor.getInt(users.getIndex("num_properties")), cursor.getInt(users.getIndex("num_castles")),
					cursor.getString(users.getIndex("name")), cursor.getString(users.getIndex("email"))));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return friendList;
	}
	
	public List<User> getFriendsAwaitingConfirmation(int userId) {
		List<User> friendList = new ArrayList<User>();
		String friendsAwaitingConfirmationQuery = String.format("SELECT id, name, email FROM Users, Requests WHERE Requests.from_user_id = %d AND Requests.to_user_id = Users.id AND Requests.type <> 0", userId);
		Cursor cursor = myDatabase.rawQuery(friendsAwaitingConfirmationQuery, null);
	
		if (cursor.moveToFirst()) {
			do {
				friendList.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return friendList;
	}
	
	public List<User> getUsersNotInFriendsList(int userId) {
		List<User> friendList = new ArrayList<User>();
		String usersNotInFriendsList = String.format("SELECT id, name, email FROM Users, Friends, Requests WHERE Users.id NOT IN (SELECT user2_id FROM Friends WHERE user1_id = %d) AND Users.id NOT IN (SELECT to_user_id FROM Requests WHERE from_user_id = %d AND Requests.type = %d)", userId, userId, FRIENDSHIP_REQUEST);
		Cursor cursor = myDatabase.rawQuery(usersNotInFriendsList, null);
	
		if (cursor.moveToFirst()) {
			do {
				friendList.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return friendList;
	}
	
	public boolean sendFriendRequest(int fromUserId, int toUserId) {
		ContentValues values = new ContentValues();
		values.put("to_user_id", toUserId);
		values.put("from_user_id", fromUserId);
		values.put("type", FRIENDSHIP_REQUEST);
		
		return myDatabase.insert(users.name, null, values) != -1;
	}
	
	public boolean removeFriend(int userId, int friendId) {
		return myDatabase.delete(friends.name,
				"user1_id = ? AND user2_id = ?",
				new String[] { String.valueOf(userId), String.valueOf(friendId) }) == 1;
	}
	
	/********************************* Groups *********************************/
	/* 
	public List<Group> getGroupsByUser(int userId) {
		List<Group> groupList = new ArrayList<Group>();
		String groupsByUserQuery = String.format("SELECT * FROM %s WHERE id IN (SELECT group_id FROM %s WHERE user_id = %d)", groups.name, usersInGroups.name, userId);
		Cursor cursor = myDatabase.rawQuery(groupsByUserQuery, null);
	
		if (cursor.moveToFirst()) {
			do {
				groupList.add(new Group(cursor.getInt(users.getIndex("id")), cursor.getString(users.getIndex("name")), cursor.getString(users.getIndex("description"))));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return groupList;
	}
	
	public List<Group> getAllGroups() {
		List<Group> groupList = new ArrayList<Group>();
		String groupsByUserQuery = String.format("SELECT * FROM %s", groups.name);
		Cursor cursor = myDatabase.rawQuery(groupsByUserQuery, null);
	
		if (cursor.moveToFirst()) {
			do {
				groupList.add(new Group(cursor.getInt(users.getIndex("id")), cursor.getString(users.getIndex("name")), cursor.getString(users.getIndex("description"))));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return groupList;
	}
	
	public boolean joinGroup(int userId, int groupId) {
		ContentValues values = new ContentValues();
		values.put("user_id", userId);
		values.put("group_id", groupId);
		
		return myDatabase.insert(groups.name, null, values) != -1;
	}
	
	public boolean leaveGroup(int userId, int groupId) {
		return myDatabase.delete(groups.name,
				"user_id = ? AND group_id = ?",
				new String[] { String.valueOf(userId), String.valueOf(groupId) }) == 1;
	}
	
	public boolean inviteFriendToJoinGroup(int userId, int friendId, int groupId) {
		ContentValues values = new ContentValues();
		values.put("from_user_id", userId);
		values.put("to_user_id", friendId);
		values.put("reference_id", groupId);
		values.put("type", GROUP_REQUEST);
		
		return myDatabase.insert(requests.name, null, values) != -1;
	} */
		
	/********************************* Challenges [6] *********************************/
	
	public List<Challenge> getAllChallenges() {
		List<Challenge> allChallenges = new ArrayList<Challenge>();
		String allChallengesQuery = String.format("SELECT * FROM %s", challenges.name);
		Cursor cursor = myDatabase.rawQuery(allChallengesQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				allChallenges.add(new Challenge(cursor.getInt(challenges.getIndex("id")), cursor.getString(challenges.getIndex("name")),
					cursor.getInt(challenges.getIndex("type")),	cursor.getString(challenges.getIndex("description"))));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return allChallenges;
	}
	
	public List<Challenge> getAllCompletedChallenges(int userId) {
		List<Challenge> allCompletedChallenges = new ArrayList<Challenge>();
		String allCompletedChallengesQuery = String.format("SELECT * FROM %s WHERE id IN (SELECT challenge_id FROM %s WHERE user_id = %d)", challenges.name, challengesCompleted.name, userId);
		Cursor cursor = myDatabase.rawQuery(allCompletedChallengesQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				allCompletedChallenges.add(new Challenge(cursor.getInt(challenges.getIndex("id")), cursor.getString(challenges.getIndex("name")),
					cursor.getInt(challenges.getIndex("type")),	cursor.getString(challenges.getIndex("description"))));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return allCompletedChallenges;
	}
	
	/********************************* MyCity [5] *********************************/
	public long addProperty(int xGP, int yGP, int x, int y, int drawableId, String title, String description, int ownerId) {
		ContentValues values = new ContentValues();
		values.put("x_coord", xGP);
		values.put("y_coord", yGP);
		values.put("x_grid", x);
		values.put("y_grid", y);
		values.put("drawable_id", drawableId);
		values.put("title", title);
		values.put("description", description);
		values.put("owner_id", ownerId);

		return myDatabase.insert(properties.name, null, values);
	}
	
	public boolean isOccupied(int x, int y) {	 
		boolean returnValue = false;
	
		Cursor cursor = myDatabase.query(properties.name,
				new String[] { "owner_id" },
				String.format("x_grid = %d AND y_grid = %d", x, y),
				null, null, null, null);
		
		returnValue = cursor.moveToFirst();
		cursor.close();
		return returnValue;
	}
	
	public void deleteAllHouses() {
		myDatabase.execSQL(String.format("DELETE FROM %s", properties.name));
	}

	public List<CustomOverlayItem> getPropertiesByUser(int userId) {
		List<CustomOverlayItem> propertyList = new ArrayList<CustomOverlayItem>();
		String allPropertiesQuery = String.format("SELECT * FROM %s WHERE owner_id = %d", properties.name, userId);
		Cursor cursor = myDatabase.rawQuery(allPropertiesQuery, null);

		if (cursor.moveToFirst()) {
			do {
				GeoPoint p = new GeoPoint(cursor.getInt(properties.getIndex("x_coord")), cursor.getInt(properties.getIndex("y_coord")));
				CustomOverlayItem item = new CustomOverlayItem(p, cursor.getString(properties.getIndex("title")), cursor.getString(properties.getIndex("description")),  cursor.getInt(properties.getIndex("id")), cursor.getInt(properties.getIndex("x_grid")), cursor.getInt(properties.getIndex("y_grid")), CustomItemizedOverlay.HOUSE);
				Drawable house = MapHelper.getDrawable(cursor.getInt(properties.getIndex("drawable_id")));
				Log.i("DRAWABLE_TYPE", String.format("%d/%d", cursor.getInt(properties.getIndex("drawable_id")), house.isVisible() ? 1 : 0));
				item.setMarker(house);
				propertyList.add(item);
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return propertyList;
	}
	
	public List<CustomOverlayItem> getFriendsPropertiesByUser(int userId) {
		List<CustomOverlayItem> propertyList = new ArrayList<CustomOverlayItem>();
		String allPropertiesQuery = String.format("SELECT * FROM %s WHERE owner_id IN (SELECT user2_id FROM %s WHERE user1_id = %d)", properties.name, friends.name, userId);
		Cursor cursor = myDatabase.rawQuery(allPropertiesQuery, null);

		if (cursor.moveToFirst()) {
			do {
				GeoPoint p = new GeoPoint(cursor.getInt(properties.getIndex("x_coord")), cursor.getInt(properties.getIndex("y_coord")));
				
				CustomOverlayItem item = new CustomOverlayItem(p, cursor.getString(properties.getIndex("title")), cursor.getString(properties.getIndex("description")),  cursor.getInt(properties.getIndex("id")), cursor.getInt(properties.getIndex("x_grid")), cursor.getInt(properties.getIndex("y_grid")), CustomItemizedOverlay.HOUSE);
				Drawable house = MapHelper.getDrawable(cursor.getInt(properties.getIndex("drawable_id")));
				item.setMarker(house);
				propertyList.add(item);
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return propertyList;
	}
	
	public List<CustomOverlayItem> getUserAndFriendsPropertiesByUser(int userId) {
		List<CustomOverlayItem> propertyList = new ArrayList<CustomOverlayItem>();
		String allPropertiesQuery = String.format("SELECT * FROM %s WHERE owner_id = %d OR owner_id IN (SELECT user2_id FROM %s WHERE user1_id = %d)", properties.name, userId, friends.name, userId);
		Cursor cursor = myDatabase.rawQuery(allPropertiesQuery, null);

		if (cursor.moveToFirst()) {
			do {
				GeoPoint p = new GeoPoint(cursor.getInt(properties.getIndex("x_coord")), cursor.getInt(properties.getIndex("y_coord")));
				
				CustomOverlayItem item = new CustomOverlayItem(p, cursor.getString(properties.getIndex("title")), cursor.getString(properties.getIndex("description")),  cursor.getInt(properties.getIndex("id")), cursor.getInt(properties.getIndex("x_grid")), cursor.getInt(properties.getIndex("y_grid")), CustomItemizedOverlay.HOUSE);
				Drawable house = MapHelper.getDrawable(cursor.getInt(properties.getIndex("drawable_id")));
				item.setMarker(house);
				propertyList.add(item);
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return propertyList;
	}
	
	public boolean itemHasBeenCollected(int userId, long collectibleID) {
		boolean returnValue = false;
		String itemHasBeenCollectedQuery = String.format("SELECT collectible_id FROM %s WHERE user_id = %d AND collectible_id = %d", collectedItems.name, userId, collectibleID);
		Cursor cursor = myDatabase.rawQuery(itemHasBeenCollectedQuery, null);
		
		if (cursor.moveToFirst()) {
			returnValue = true;
		}
		cursor.close();
		return returnValue;
	}
			
	public boolean collectItem(int userId, long collectibleId) {
		ContentValues values = new ContentValues();
		values.put("user_id", userId);
		values.put("collectible_id", collectibleId);
		
		return myDatabase.insert(collectedItems.name, null, values) != -1;
	}
	
	public boolean useItem(int userId, long collectibleId) {
		ContentValues values = new ContentValues();
		values.put("renewable_after_day", Calendar.DAY_OF_YEAR);
		values.put("renewable_after_year", Calendar.YEAR);

		return myDatabase.update(collectedItems.name, values,
				"user_id = ? AND collectible_id = ? AND renewable_after_day = -1 AND renewable_after_year = -1",
				new String[] { String.valueOf(userId), String.valueOf(collectibleId) }) == 1;		
	}
	
	public boolean renewItem(int userId, long collectibleId) {
		ContentValues values = new ContentValues();
		values.put("renewable_after_day", -1);
		values.put("renewable_after_year", -1);

		return myDatabase.update(collectedItems.name, values,
				String.format("user_id = ? AND collectible_id = ? AND (renewable_after_day <> %d OR renewable_after_year <> %d)",
						Calendar.DAY_OF_YEAR, Calendar.YEAR),
				new String[] { String.valueOf(userId), String.valueOf(collectibleId) }) == 1;		
	}

	public ParkCard drawParkCard(int userId, long collectibleId) {
		boolean itemIsUsable = useItem(userId, collectibleId);
		int chanceCardType = getRandomInt(0, NUM_CHANCE_CARD_ITEM_TYPES); // get a random card { MyCity points, unlockable avatar, unlockable accessories, unlockable challenges }
		ParkCard cardDrawn = null;
		
		switch (chanceCardType) {
		case MYCITY_POINTS:
			int amount = MONEY_GIVEAWAY_AMOUNTS[getRandomInt(0, MONEY_GIVEAWAY_AMOUNTS.length)]; 
			cardDrawn = new ParkCard(MYCITY_POINTS, amount);
			break;
		/* case UNLOCKED_AVATAR:
			int avatarId = 1; // TODO: select a random integer from a list of unlocked avatar IDs
			cardDrawn = new ParkCard(UNLOCKED_AVATAR, avatarId);
			break; */
		}
		
		return itemIsUsable ? cardDrawn : null;
	}
	
	public boolean updateUserMyCityPointsAndScore(int userId, int points, int score) {
		ContentValues values = new ContentValues();
		values.put("mycity_points", points);
		values.put("overall_score", score);
		
		return myDatabase.update(users.name, values,
				"id = ?",
				new String[] { String.valueOf(userId) }) == 1;
	}
	
	/********************************* Requests [6] *********************************/
	
	public boolean respondToRequest(int fromUserId, int toUserId, int type, boolean acceptRequest) {
		ContentValues values = new ContentValues();
		values.put("response", acceptRequest ? 1 : 2);
		
		return myDatabase.update(requests.name, values,
			"from_user_id = ? AND to_user_id = ? AND type = ?",
			new String[] { String.valueOf(fromUserId), String.valueOf(toUserId), String.valueOf(type) }) == 1;
	}
	
	/********************************* Collectibles [7] *********************************/
	
	public boolean referencesNotUpdated() {
		boolean returnValue = false;
		String referencesUpdatedQuery = String.format("SELECT drawable_id FROM %s WHERE drawable_id = 0", collectibles.name);
		Cursor cursor = myDatabase.rawQuery(referencesUpdatedQuery, null);
		
		if (cursor.moveToFirst()) {
			returnValue = true;
		}
		cursor.close();
		return returnValue;
	}
	
	public boolean updateAllCollectibleDrawablesReferences() {
		boolean returnValue = false;
		
		returnValue &= updateReference("100 MyCity Points", R.drawable.collectible_mycity_points_100);
		returnValue &= updateReference("250 MyCity Points", R.drawable.collectible_mycity_points_250);
		returnValue &= updateReference("500 MyCity Points", R.drawable.collectible_mycity_points_500);
		returnValue &= updateReference("1000 MyCity Points", R.drawable.collectible_mycity_points_1000);
		returnValue &= updateReference("2500 MyCity Points", R.drawable.collectible_mycity_points_2500);
		returnValue &= updateReference("5000 MyCity Points", R.drawable.collectible_mycity_points_5000);
		
		returnValue &= updateReference("Riverside Museum City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("Kelvingrove Art Gallery and Museum City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("The Burrell Collection City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("Gallery of Modern Art (GoMA) City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("Provand's Lordship City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("People's Palace City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("Scotland Street School Museum City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("St Mungo Museum of Religious Life and Art City Card", R.drawable.collectible_city_card);
		returnValue &= updateReference("Glasgow Museums Resource Centre (GMRC) City Card", R.drawable.collectible_city_card);
		
		returnValue &= updateParkCardsReferences(R.drawable.collectible_park_card);
		return returnValue;
	}
	
	private boolean updateReference(String name, int drawableId) {
		ContentValues values = new ContentValues();
		values.put("drawable_id", drawableId);
		
		return myDatabase.update(collectibles.name, values,
			"name = ?",
			new String[] { name }) == 1;
	}
	
	private boolean updateParkCardsReferences(int drawableId) {
		ContentValues values = new ContentValues();
		values.put("drawable_id", drawableId);
		
		return myDatabase.update(collectibles.name, values,
			"type = 1",
			new String[] {}) == 1;
	}
	
	public List<Collectible> getUserCollectedItems(int userId) {
		List<Collectible> userCollectedItems = new ArrayList<Collectible>();
		String userCollectedItemsQuery = String.format("SELECT id, x_coord, y_coord, x_grid, y_grid, name, type, description, drawable_id, quantity FROM %s, %s WHERE id = collectible_id AND user_id = %d", collectibles.name, collectedItems.name, userId);
		Cursor cursor = myDatabase.rawQuery(userCollectedItemsQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				userCollectedItems.add(new Collectible(cursor.getInt(collectibles.getIndex("id")), cursor.getInt(collectibles.getIndex("x_coord")), cursor.getInt(collectibles.getIndex("y_coord")),
						cursor.getInt(collectibles.getIndex("x_grid")), cursor.getInt(collectibles.getIndex("y_grid")),
						cursor.getString(collectibles.getIndex("name")), cursor.getInt(collectibles.getIndex("type")),
						cursor.getString(collectibles.getIndex("description")), cursor.getInt(collectibles.getIndex("drawable_id")),
						cursor.getInt(collectibles.getIndex("quantity"))));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return userCollectedItems;
	}
	
	/* public List<Collectible> getGroupCollectedItems(int groupId) {
		List<Collectible> groupCollectedItems = new ArrayList<Collectible>();
		String groupCollectedItemsQuery = String.format("SELECT id, x_coord, y_coord, x_grid, y_grid, name, type, description, drawable_id, quantity FROM %s AS c, %s AS ci, %s AS uig WHERE id = collectible_id AND ci.user_id = uig.user_id AND ug.group_id = %d", collectibles.name, collectedItems.name, usersInGroups.name, groupId);
		Cursor cursor = myDatabase.rawQuery(groupCollectedItemsQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				groupCollectedItems.add(new Collectible(cursor.getInt(collectibles.getIndex("id")), cursor.getInt(collectibles.getIndex("x_coord")), cursor.getInt(collectibles.getIndex("y_coord")),
						cursor.getInt(collectibles.getIndex("x_grid")), cursor.getInt(collectibles.getIndex("y_grid")),
						cursor.getString(collectibles.getIndex("name")), cursor.getInt(collectibles.getIndex("type")),
						cursor.getString(collectibles.getIndex("description")), cursor.getInt(collectibles.getIndex("drawable_id")),
						cursor.getInt(collectibles.getIndex("quantity"))));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return groupCollectedItems;
	} */
	
	public List<Collectible> getUserNotYetCollectedItems(int userId) {
		List<Collectible> notYetCollectedItems = new ArrayList<Collectible>();
		String notYetCollectedItemsQuery = String.format("SELECT * FROM %s WHERE id NOT IN (SELECT collectible_id FROM %s WHERE user_id = %d) ORDER BY name ASC", collectibles.name, collectedItems.name, userId);
		Cursor cursor = myDatabase.rawQuery(notYetCollectedItemsQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				notYetCollectedItems.add(new Collectible(cursor.getInt(collectibles.getIndex("id")), cursor.getInt(collectibles.getIndex("x_coord")), cursor.getInt(collectibles.getIndex("y_coord")),
						cursor.getInt(collectibles.getIndex("x_grid")), cursor.getInt(collectibles.getIndex("y_grid")),
						cursor.getString(collectibles.getIndex("name")), cursor.getInt(collectibles.getIndex("type")),
						cursor.getString(collectibles.getIndex("description")), cursor.getInt(collectibles.getIndex("drawable_id")),
						cursor.getInt(collectibles.getIndex("quantity"))));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return notYetCollectedItems;
	}
	
	// get all collectibles of a given type that have not yet been collected by a given user
	public List<Integer> getAllCollectibleIDsNotYetCollectedOfType(int type, int userId) {
		List<Integer> allCollectibleIDsOfType = new ArrayList<Integer>();
		String allCollectiblesOfTypeQuery = String.format("SELECT id FROM %s WHERE type = %d AND id NOT IN (SELECT collectible_id FROM %s WHERE user_id = %d)", type, collectibles.name, collectedItems.name, userId);
		Cursor cursor = myDatabase.rawQuery(allCollectiblesOfTypeQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				allCollectibleIDsOfType.add(cursor.getInt(0));	
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return allCollectibleIDsOfType;
	}
	
	public List<CustomOverlayItem> getAllCollectiblesOfType(int type) {
		List<CustomOverlayItem> allCollectiblesOfType = new ArrayList<CustomOverlayItem>();
		String allCollectiblesOfTypeQuery = String.format("SELECT * FROM %s WHERE type = %d", collectibles.name, type);
		Cursor cursor = myDatabase.rawQuery(allCollectiblesOfTypeQuery, null);
		
		if (cursor.moveToFirst()) {
			do {				
				GeoPoint p = new GeoPoint(cursor.getInt(collectibles.getIndex("x_coord")), cursor.getInt(collectibles.getIndex("y_coord")));
				CustomOverlayItem item = new CustomOverlayItem(p, cursor.getString(collectibles.getIndex("name")), cursor.getString(collectibles.getIndex("description")), cursor.getInt(collectibles.getIndex("id")), cursor.getInt(collectibles.getIndex("x_grid")), cursor.getInt(collectibles.getIndex("y_grid")), type, cursor.getInt(collectibles.getIndex("quantity")));
				Drawable house = MapHelper.getDrawable(Collectible.CARD_DRAWABLE_IDS[cursor.getInt(collectibles.getIndex("type"))]);
				Log.i("DRAWABLE_TYPE", String.format("Card drawable ID: %d", Collectible.CARD_DRAWABLE_IDS[cursor.getInt(collectibles.getIndex("type"))]));
				item.setMarker(house);
				allCollectiblesOfType.add(item);
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return allCollectiblesOfType;
	}
		
	/********************************* Rankings [8] *********************************/
	
	public List<Record> getOverallRankings() {
		List<Record> overallRankings = new ArrayList<Record>();
		String overallRankingsQuery = String.format("SELECT name, overall_score, num_castles, num_properties FROM %s ORDER BY overall_score DESC", users.name);
		Cursor cursor = myDatabase.rawQuery(overallRankingsQuery, null);

		if (cursor.moveToFirst()) {
			do {
				overallRankings.add(new Record(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
			}
			while (cursor.moveToNext());
		}
		return overallRankings;
	}
	
	/* public List<Record> getByGroupRankings(int groupId, int userId) {
		List<Record> byGroupRankings = new ArrayList<Record>();
		String byGroupRankingsQuery = String.format("SELECT name, overall_score, num_castles, num_properties FROM %s, %s WHERE id = user_id AND group_id = %d ORDER BY overall_score DESC", users.name, usersInGroups.name, groupId);
		Cursor cursor = myDatabase.rawQuery(byGroupRankingsQuery, null);

		if (cursor.moveToFirst()) {
			do {
				byGroupRankings.add(new Record(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
			}
			while (cursor.moveToNext());
		}
		return byGroupRankings;
	} */
	
	public List<Record> getMyFriendsRankings(int userId) {
		List<Record> myFriendsRankings = new ArrayList<Record>();
		String myFriendsRankingsQuery = String.format("SELECT name, overall_score, num_castles, num_properties FROM %s, %s WHERE id = user2_id AND user1_id = %d ORDER BY overall_score DESC", friends.name, users.name, userId);
		Cursor cursor = myDatabase.rawQuery(myFriendsRankingsQuery, null);

		if (cursor.moveToFirst()) {
			do {
				myFriendsRankings.add(new Record(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return myFriendsRankings;
	}
	
	public static List<String> getRankingsUserNames(List<Record> records) {
		List<String> displayNames = new ArrayList<String>();
		for (Record r : records) {
			displayNames.add(r.getDisplayName());
		}
		return displayNames;
	}
	
	public static List<Integer> getRankingsOverallScores(List<Record> records) {
		List<Integer> overallScores = new ArrayList<Integer>();
		for (Record r : records) {
			overallScores.add(r.getOverallScore());
		}
		return overallScores;
	}
	
	public static List<Integer> getRankingsNumCastles(List<Record> records) {
		List<Integer> numCastles = new ArrayList<Integer>();
		for (Record r : records) {
			numCastles.add(r.getNumCastles());
		}
		return numCastles;
	}
	
	public static List<Integer> getRankingsNumProperties(List<Record> records) {
		List<Integer> numProperties = new ArrayList<Integer>();
		for (Record r : records) {
			numProperties.add(r.getNumProperties());
		}
		return numProperties;
	}
}