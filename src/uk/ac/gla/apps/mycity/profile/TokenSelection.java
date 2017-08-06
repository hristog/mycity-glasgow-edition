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

package uk.ac.gla.apps.mycity.profile;

import uk.ac.gla.apps.mycity.R;

import uk.ac.gla.apps.mycity.account.Menu;
import uk.ac.gla.apps.mycity.collectibles.CollectibleList;
import uk.ac.gla.apps.mycity.helper.ImageAdapter;
import uk.ac.gla.apps.mycity.helper.User;
import uk.ac.gla.apps.mycity.map.MapHelper;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.util.Log;
import android.view.View;

public class TokenSelection extends ListActivity {
	private static final int[] tokenIds = { R.drawable.token_default, R.drawable.token_1, R.drawable.token_2, R.drawable.token_3,
    	R.drawable.token_4, R.drawable.token_5, R.drawable.token_6,
    	R.drawable.token_7, R.drawable.token_8, R.drawable.token_9, R.drawable.token_10,
    	R.drawable.token_11, R.drawable.token_12, R.drawable.token_13,
    	R.drawable.token_14, R.drawable.token_15, R.drawable.token_16,
    	R.drawable.token_17, R.drawable.token_18, R.drawable.token_19, R.drawable.token_20,
    	R.drawable.token_21 };
	
	private User user;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		user = null;
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	user = extras.getParcelable("user");
        }
        
	    String[] captions = new String[tokenIds.length];
	    for (int i = 0; i < captions.length; i++) {
	    	captions[i] = "Avatar " + (i+1);
	    }
        
    	ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		setListAdapter(new ImageAdapter(this, tokenIds, captions, R.layout.profile_list));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
	        	Intent intent = new Intent(TokenSelection.this, Menu.class);
	        	intent.putExtra("token_id", tokenIds[pos]);
	        	Log.i("TOKEN_ID", String.format("%d", tokenIds[pos]));
	        	Log.i("USER_TOKEN_ID", String.format("%d", user.getTokenId()));
	        	intent.putExtra("user", user);
    			startActivityForResult(intent, 0);
			}
		});
	}
	
	public static int[] getTokenIds() {
		return tokenIds;
	}
}