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

package uk.ac.gla.apps.mycity.requests;

import uk.ac.gla.apps.mycity.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Requests extends TabActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tab);
      
      final String FRIEND_CAPTION = String.format("Friend (%d)", 0);
      final String CHALLENGE_CAPTION = String.format("Challenge (%d)", 0);
      // final String GROUP_CAPTION = String.format("Group (%d)", 0);

      TabHost tabHost = getTabHost();

      TabSpec friendRequestsSpec = tabHost.newTabSpec(FRIEND_CAPTION);
      friendRequestsSpec.setIndicator(FRIEND_CAPTION, getResources().getDrawable(R.drawable.icon_log_tab));
      Intent friendRequestsIntent = new Intent(this, RequestList.class);
      friendRequestsSpec.setContent(friendRequestsIntent);

      TabSpec challengeRequestsSpec = tabHost.newTabSpec(CHALLENGE_CAPTION);
      challengeRequestsSpec.setIndicator(CHALLENGE_CAPTION, getResources().getDrawable(R.drawable.icon_visualise_tab));
      Intent challengeRequestsIntent = new Intent(this, RequestList.class);
      challengeRequestsSpec.setContent(challengeRequestsIntent);
      
      /* TabSpec groupRequestsSpec = tabHost.newTabSpec(GROUP_CAPTION);
      groupRequestsSpec.setIndicator(GROUP_CAPTION, getResources().getDrawable(R.drawable.icon_visualise_tab));
      Intent groupRequestsIntent = new Intent(this, RequestList.class);
      groupRequestsSpec.setContent(groupRequestsIntent); */
      
      tabHost.addTab(friendRequestsSpec); // Adding friend tab
      tabHost.addTab(challengeRequestsSpec); // Adding challenge tab
      // tabHost.addTab(groupRequestsSpec); // Adding group tab
  }
}