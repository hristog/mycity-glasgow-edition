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

package uk.ac.gla.apps.mycity.collectibles;

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.helper.ImageAdapter;
import uk.ac.gla.apps.mycity.helper.User;
import uk.ac.gla.apps.mycity.map.MapHelper;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class CollectibleList extends ListActivity {
	private String[] collectibleNames;
	private String[] collectibleDescriptions;
	private int[] collectibleDrawableIds;
	
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		User user = null;
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	user = extras.getParcelable("user");
        	collectibleDrawableIds = extras.getIntArray("collectible_drawable_ids");
        	collectibleNames = extras.getStringArray("collectible_names");
        	collectibleDescriptions = extras.getStringArray("collectible_descriptions");
        }
        
    	ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		setListAdapter(new ImageAdapter(this, collectibleDrawableIds, collectibleNames, R.layout.collectibles_list));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				MapHelper.showDialog(CollectibleList.this, collectibleNames[pos], collectibleDescriptions[pos]);
			}
		});
	}
}