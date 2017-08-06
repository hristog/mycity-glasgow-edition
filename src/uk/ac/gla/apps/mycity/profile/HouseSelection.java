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
import uk.ac.gla.apps.mycity.helper.ImageAdapter;
import uk.ac.gla.apps.mycity.helper.User;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class HouseSelection extends ListActivity {
	private static final int[] houseTypeIds = { R.drawable.house_default, R.drawable.house_1, R.drawable.house_2,
			R.drawable.house_3, R.drawable.house_4, R.drawable.house_5,
			R.drawable.house_6, R.drawable.house_7, R.drawable.house_8,
			R.drawable.house_9 };
	
	private User user;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		user = null;
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	user = extras.getParcelable("user");
        }
        
	    String[] captions = new String[houseTypeIds.length];
	    for (int i = 0; i < captions.length; i++) {
	    	captions[i] = "Property Type " + (i+1);
	    }
        
    	ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		setListAdapter(new ImageAdapter(this, houseTypeIds, captions, R.layout.profile_list));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
	        	Intent intent = new Intent(HouseSelection.this, Menu.class);
	        	intent.putExtra("house_type_id", houseTypeIds[pos]);
	        	intent.putExtra("user", user);
    			startActivityForResult(intent, 0);
			}
		});
	}
	
	public static int[] getHouseTypeIds() {
		return houseTypeIds;
	}
}