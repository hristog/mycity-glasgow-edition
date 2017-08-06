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

package uk.ac.gla.apps.mycity.friends;

import java.util.List;

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.User;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Friends extends TabActivity {
	private DatabaseHelper dbHelper;
	private List<User> allFriends;
	private List<User> awaitingConfirmation;
	private List<User> notInFriendsList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);

		User user = null;
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	user = extras.getParcelable("user");
        }
        
		final int userId = user.getId();
		
		dbHelper = new DatabaseHelper(this);
		allFriends = dbHelper.getAllFriends(userId);
		awaitingConfirmation = dbHelper.getFriendsAwaitingConfirmation(userId);
		notInFriendsList = dbHelper.getUsersNotInFriendsList(userId);
		
		dbHelper.close();
		
		final String ALL_FRIENDS_CAPTION = String.format("All (%d)", allFriends.size());
        final String AWAITING_CONFIRMATION_CAPTION = String.format("Awaiting Confirmation (%d)", awaitingConfirmation.size());
        final String NOT_IN_LIST_CAPTION = String.format("Not In List (%d)", notInFriendsList.size());
		
		TabHost tabHost = getTabHost();

		TabSpec allFriendsSpec = tabHost.newTabSpec(ALL_FRIENDS_CAPTION);
		allFriendsSpec.setIndicator(ALL_FRIENDS_CAPTION, getResources().getDrawable(R.drawable.icon_log_tab));
		Intent allFriendsIntent = new Intent(this, FriendsList.class);
		allFriendsIntent.putExtra("user_ids", User.getIds(allFriends));
		allFriendsIntent.putExtra("token_ids", User.getTokenIds(allFriends));
		allFriendsIntent.putExtra("user_names", User.getNames(allFriends));
		allFriendsIntent.putExtra("user", user);
		allFriendsSpec.setContent(allFriendsIntent);
		
		TabSpec awaitingConfirmationSpec = tabHost.newTabSpec(AWAITING_CONFIRMATION_CAPTION);
		awaitingConfirmationSpec.setIndicator(AWAITING_CONFIRMATION_CAPTION, getResources().getDrawable(R.drawable.icon_visualise_tab));
		Intent awaitingConfirmationIntent = new Intent(this, FriendsList.class);
		awaitingConfirmationIntent.putExtra("user_ids", User.getIds(awaitingConfirmation));
		awaitingConfirmationIntent.putExtra("token_ids", User.getTokenIds(awaitingConfirmation.size()));
		awaitingConfirmationIntent.putExtra("user_names", User.getNames(awaitingConfirmation));
		awaitingConfirmationIntent.putExtra("user", user);
		awaitingConfirmationSpec.setContent(awaitingConfirmationIntent);
		
		TabSpec notInListSpec = tabHost.newTabSpec(NOT_IN_LIST_CAPTION);
		notInListSpec.setIndicator(NOT_IN_LIST_CAPTION, getResources().getDrawable(R.drawable.icon_summary_tab));
		Intent notInListIntent = new Intent(this, FriendsList.class);
		notInListIntent.putExtra("user_ids", User.getIds(notInFriendsList));
		notInListIntent.putExtra("token_ids", User.getTokenIds(notInFriendsList.size()));
		notInListIntent.putExtra("user_names", User.getNames(notInFriendsList));
		notInListIntent.putExtra("user", user);
		notInListSpec.setContent(notInListIntent);
		
		tabHost.addTab(allFriendsSpec);
		tabHost.addTab(awaitingConfirmationSpec);
		tabHost.addTab(notInListSpec);
	}
}