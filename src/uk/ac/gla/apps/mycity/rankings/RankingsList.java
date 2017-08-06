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

package uk.ac.gla.apps.mycity.rankings;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.Record;
import uk.ac.gla.apps.mycity.helper.RecordAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class RankingsList extends ListActivity {
	private List<Record> rankings;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	    	rankings = extras.getParcelableArrayList("rankings");
	    }
	    else {
	    	rankings = new ArrayList<Record>();
	    }
	    
		setListAdapter(new RecordAdapter(RankingsList.this, R.layout.rankings, rankings));
	}
}