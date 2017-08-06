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

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.helper.ImageAdapter;
import uk.ac.gla.apps.mycity.helper.User;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class FriendsList extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		User user = null;
		int[] userIds = null, tokenIds = null;
		String[] userNames = null;
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	user = extras.getParcelable("user");
        	userIds = extras.getIntArray("user_ids");
        	tokenIds = extras.getIntArray("token_ids");
        	userNames = extras.getStringArray("user_names");
        }
        
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		setListAdapter(new ImageAdapter(this, tokenIds, userNames, R.layout.list));
	}
}