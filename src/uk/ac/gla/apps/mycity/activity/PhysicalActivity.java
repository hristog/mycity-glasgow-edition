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

package uk.ac.gla.apps.mycity.activity;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gla.apps.mycity.helper.Activity;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.User;
import uk.ac.gla.apps.mycity.helper.Week;

import uk.ac.gla.apps.mycity.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class PhysicalActivity extends TabActivity {
	private DatabaseHelper dbHelper;
	private List<Activity> userActivity;
	private List<Week> userActiveWeeks;
	
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
		userActivity = dbHelper.getOverallActivity(userId);
		userActiveWeeks = dbHelper.getWeekListByUser(userId);
		dbHelper.close();
        
        final String LOG_ACTIVITY_CAPTION = "Log activity";
        final String WEEKLY_ACTIVITY_CAPTION = String.format("Weekly (%d)", userActiveWeeks.size());
        final String SUMMARY_CAPTION = "Summary";

        TabHost tabHost = getTabHost();
        
        List<Activity> activityList1 = new ArrayList<Activity>();
        activityList1.add(new Activity(1, 33, 2012, 1, 50));
        activityList1.add(new Activity(1, 33, 2012, 2, 25));
        activityList1.add(new Activity(1, 33, 2012, 3, 0));       
        activityList1.add(new Activity(1, 33, 2012, 4, 0));
        
        PieGraph pie = new PieGraph(activityList1, "Weekly Activity");
    	/*Intent lineIntent = pie.getIntent(this);
        startActivity(lineIntent);*/

        TabSpec logActivitySpec = tabHost.newTabSpec(LOG_ACTIVITY_CAPTION);
        logActivitySpec.setIndicator(LOG_ACTIVITY_CAPTION, getResources().getDrawable(R.drawable.icon_summary_tab));
        Intent logActivityIntent = new Intent(this, LogActivity.class);
        logActivitySpec.setContent(logActivityIntent);
        
        TabSpec weeklyActivitySpec = tabHost.newTabSpec(WEEKLY_ACTIVITY_CAPTION);
        weeklyActivitySpec.setIndicator(WEEKLY_ACTIVITY_CAPTION, getResources().getDrawable(R.drawable.icon_summary_tab));
        Intent weekyActivityIntent = new Intent(this, WeekList.class);
        weekyActivityIntent.putExtra("user", user);
        weekyActivityIntent.putExtra("weeks", Week.getWeeks(userActiveWeeks));
        weekyActivityIntent.putExtra("years", Week.getYears(userActiveWeeks));
        weekyActivityIntent.putExtra("captions", Week.getCaptions(userActiveWeeks));
        weeklyActivitySpec.setContent(weekyActivityIntent);
        
        TabSpec summarySpec = tabHost.newTabSpec(SUMMARY_CAPTION);
        summarySpec.setIndicator(SUMMARY_CAPTION, getResources().getDrawable(R.drawable.icon_summary_tab));
        Intent summaryIntent = pie.getIntent(this);
        summarySpec.setContent(summaryIntent);

        tabHost.addTab(logActivitySpec);
        tabHost.addTab(weeklyActivitySpec);
        tabHost.addTab(summarySpec);
    }
}