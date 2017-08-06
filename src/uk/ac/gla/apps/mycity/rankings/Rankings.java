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
import uk.ac.gla.apps.mycity.helper.User;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Rankings extends TabActivity {
	private DatabaseHelper dbHelper;
	private List<Record> overallRankings;
	private List<Record> myFriendsRankings;
	
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
		overallRankings = dbHelper.getOverallRankings();
		myFriendsRankings = dbHelper.getMyFriendsRankings(userId);
		
		dbHelper.close();
		
		final String OVERALL_CAPTION = String.format("Overall (%d)", overallRankings.size());
	    final String MY_FRIENDS_CAPTION = String.format("My Friends (%d)", myFriendsRankings.size());
	    
	    Log.i("RANKINGS", OVERALL_CAPTION);
	    Log.i("RANKINGS", MY_FRIENDS_CAPTION);

	    TabHost tabHost = getTabHost();
	    
	    /* Record[] overallRankingsArray = new Record[overallRankings.size()];
	    Record[] myFriendsRankingsArray = new Record[myFriendsRankings.size()];
	    overallRankings.toArray(overallRankingsArray);
	    myFriendsRankings.toArray(myFriendsRankingsArray);*/

      	TabSpec overallRankingsSpec = tabHost.newTabSpec(OVERALL_CAPTION);
      	overallRankingsSpec.setIndicator(OVERALL_CAPTION, getResources().getDrawable(R.drawable.icon_log_tab));
      	Intent overallRankingsIntent = new Intent(Rankings.this, RankingsList.class);
      	overallRankingsIntent.putExtra("rankings", (ArrayList<Record>) overallRankings);
      	overallRankingsSpec.setContent(overallRankingsIntent);

      	TabSpec myFriendsRankingsSpec = tabHost.newTabSpec(MY_FRIENDS_CAPTION);
      	myFriendsRankingsSpec.setIndicator(MY_FRIENDS_CAPTION, getResources().getDrawable(R.drawable.icon_visualise_tab));
      	Intent myFriendsRankingsIntent = new Intent(Rankings.this, RankingsList.class);
      	myFriendsRankingsIntent.putExtra("rankings", (ArrayList<Record>) myFriendsRankings);
      	myFriendsRankingsSpec.setContent(myFriendsRankingsIntent);
      
      	tabHost.addTab(overallRankingsSpec);
      	tabHost.addTab(myFriendsRankingsSpec);
   }
}	