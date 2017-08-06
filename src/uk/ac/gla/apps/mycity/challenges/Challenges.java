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

package uk.ac.gla.apps.mycity.challenges;

import uk.ac.gla.apps.mycity.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Challenges extends TabActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tab);
      
      final String ALL_CHALLENGES_CAPTION = String.format("All (%d)", 0);
      final String COMPLETED_CAPTION = String.format("Completed (%d)", 0);

      TabHost tabHost = getTabHost();

      TabSpec allChallengesSpec = tabHost.newTabSpec(ALL_CHALLENGES_CAPTION);
      allChallengesSpec.setIndicator(ALL_CHALLENGES_CAPTION, getResources().getDrawable(R.drawable.icon_log_tab));
      Intent allChallengesIntent = new Intent(this, ChallengeList.class);
      allChallengesSpec.setContent(allChallengesIntent);
      
      TabSpec completedSpec = tabHost.newTabSpec(COMPLETED_CAPTION);
      completedSpec.setIndicator(COMPLETED_CAPTION, getResources().getDrawable(R.drawable.icon_visualise_tab));
      Intent completedIntent = new Intent(this, ChallengeList.class);
      completedSpec.setContent(completedIntent);

      tabHost.addTab(allChallengesSpec); // Adding all tab
      tabHost.addTab(completedSpec); // Adding completed tab
  }
}