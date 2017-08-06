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

package uk.ac.gla.apps.mycity.account;

import uk.ac.gla.apps.mycity.R;

import uk.ac.gla.apps.mycity.activity.PhysicalActivity;
import uk.ac.gla.apps.mycity.challenges.Challenges;
import uk.ac.gla.apps.mycity.collectibles.Collectibles;
import uk.ac.gla.apps.mycity.friends.Friends;
import uk.ac.gla.apps.mycity.help.About;
import uk.ac.gla.apps.mycity.help.Help;
import uk.ac.gla.apps.mycity.help.HelpAbout;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.ImageAdapter;
import uk.ac.gla.apps.mycity.helper.User;
import uk.ac.gla.apps.mycity.map.MapHelper;
import uk.ac.gla.apps.mycity.map.MyCity;
import uk.ac.gla.apps.mycity.profile.Profile;
import uk.ac.gla.apps.mycity.rankings.Rankings;
import uk.ac.gla.apps.mycity.requests.Requests;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

public class Menu extends Activity {
	private User user;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    final int[] charDrawableIds = { R.drawable.menu_activity, R.drawable.menu_profile, R.drawable.menu_friends,
		    	R.drawable.menu_challenges, R.drawable.menu_mycity, R.drawable.menu_requests,
		    	R.drawable.menu_collectibles, R.drawable.menu_rankings, R.drawable.menu_help };
	    
	    final String[] captions = { "Activity", "Profile", "Friends",
	    		"Challenges", "My City", "Requests",
	    		"Collectibles", "Rankings", "Help" };
    		    
	    GridView gridView = (GridView) findViewById(R.id.main_gridView);
	    gridView.setAdapter(new ImageAdapter(this, charDrawableIds, captions, R.layout.menu_grid));
	    
	    int mode = -1;
	    
	    user = null;
	    
	    Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	mode = extras.getInt("mode");
        	user = extras.getParcelable("user");
        	
        	int tokenId = -1;
    	    int houseTypeId = -1;
    	    
    	    DatabaseHelper dbHelper = new DatabaseHelper(Menu.this);
        	
    	    if (user == null) {
    	    	throw new NullPointerException("user is NULL");
    	    }
    	    
    	    
        	if (extras.containsKey("token_id")) {
        		tokenId = extras.getInt("token_id");
        		Log.i("TOKEN_ID_MENU", String.format("%d", tokenId));
        		dbHelper.updateUserToken(user.getId(), tokenId);
        		user.setTokenId(tokenId);
        	}
        	
        	if (extras.containsKey("house_type_id")) {
        		houseTypeId = extras.getInt("house_type_id");
        		dbHelper.updateUserHouseType(user.getId(), houseTypeId);
        		user.setHouseType(houseTypeId);
        	}
        	
        	dbHelper.close();
        }
        else {
        	user = new User();
        }
        
        final int currentMode = mode;

        TextView textView_userName = (TextView) findViewById(R.id.mainMenu_userName);
        Button button_helpAbout = (Button) findViewById(R.id.menu_button_helpAbout);
        Button button_logOut = (Button) findViewById(R.id.menu_button_logOut);
        
        View.OnClickListener menuListener = new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = null;
				switch(v.getId()) {
				case (R.id.menu_button_helpAbout):
					intent = new Intent(Menu.this, About.class);
					intent.putExtra("user", user);
					break;
				case (R.id.menu_button_logOut):
					intent = new Intent(Menu.this, Welcome.class);
					break;
				}
				startActivityForResult(intent, 0);
			}
        };
        
        button_helpAbout.setOnClickListener(menuListener);
        button_logOut.setOnClickListener(menuListener);
	    
	    textView_userName.setText(user.getDisplayName());

	    gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent menuChoice = null;
	            switch (position) {
	            case 0: // Activity
	            	menuChoice = new Intent(Menu.this, PhysicalActivity.class);
	            	break;
	            case 1: // Profile
	            	menuChoice = new Intent(Menu.this, Profile.class);
        			break;
	            case 2: // Friends
	            	menuChoice = new Intent(Menu.this, Friends.class);
	            	break;
	            case 3: // Challenges
	            	//menuChoice = new Intent(Menu.this, Challenges.class);
	            	break;
	            case 4: // My City
	            	if (isNetworkAvailable()) {
	            		menuChoice = new Intent(Menu.this, MyCity.class);
	            	}
	            	else {
	            		MapHelper.showDialog(Menu.this, "No Internet Access Detected", "Unfortunately, no internet access has been detected. This restricts the list of available features and this menu option is one of the features that cannot be accessed without a working internet connection.");
	            	}
	            	break;
	            case 5: // Requests
	            	//menuChoice = new Intent(Menu.this, Requests.class);
	            	break;
	            case 6: // Collectibles
	            	menuChoice = new Intent(Menu.this, Collectibles.class);
	            	break;
	            case 7: // Rankings
	            	//menuChoice = new Intent(Menu.this, Rankings.class);
	            	break;
	            case 8: // Help
	            	menuChoice = new Intent(Menu.this, Help.class);
	            	break;
	            }
	            
	            if (menuChoice != null) {
	            	menuChoice.putExtra("user", user);
	            	menuChoice.putExtra("mode", currentMode);
	            	startActivityForResult(menuChoice, 0);
	            }
	        }
	    });
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
}