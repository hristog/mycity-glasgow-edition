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
import uk.ac.gla.apps.mycity.helper.User;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Profile extends TabActivity {
	private User user;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tab);
    
      user = null;
      Bundle extras = getIntent().getExtras();
      if (extras != null) {
      	user = extras.getParcelable("user");
      }

      TabHost tabHost = getTabHost();

      // Tab for Token Activity
      TabSpec tokenSpec = tabHost.newTabSpec("Token");
      tokenSpec.setIndicator("Token", getResources().getDrawable(R.drawable.icon_log_tab));
      Intent tokenIntent = new Intent(this, TokenSelection.class);
      tokenIntent.putExtra("user", user);
      tokenSpec.setContent(tokenIntent);

      // Tab for Building Type Activity
      TabSpec buildingTypeSpec = tabHost.newTabSpec("Building Type");
      buildingTypeSpec.setIndicator("Building Type", getResources().getDrawable(R.drawable.icon_visualise_tab));
      Intent buildingTypeIntent = new Intent(this, HouseSelection.class);
      buildingTypeIntent.putExtra("user", user);
      buildingTypeSpec.setContent(buildingTypeIntent);
      
      // Tab for By Group Rankings Activity
      TabSpec settingsSpec = tabHost.newTabSpec("Settings");
      settingsSpec.setIndicator("Settings", getResources().getDrawable(R.drawable.icon_visualise_tab));
      Intent settingsIntent = new Intent(this, AccountSettings.class);
      settingsIntent.putExtra("user", user);
      settingsSpec.setContent(settingsIntent);
      
      // Adding all TabSpec to TabHost
      tabHost.addTab(tokenSpec); // Adding token tab
      tabHost.addTab(buildingTypeSpec); // Adding building tab
      tabHost.addTab(settingsSpec); // Adding settings tab
  }
}