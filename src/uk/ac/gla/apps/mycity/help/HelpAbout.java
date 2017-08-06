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

package uk.ac.gla.apps.mycity.help;

import uk.ac.gla.apps.mycity.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class HelpAbout extends TabActivity {
	  
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tab);
      
      final String HELP_CAPTION = "Help";
      final String ABOUT_CAPTION = "About";

      TabHost tabHost = getTabHost();

      TabSpec helpSpec = tabHost.newTabSpec(HELP_CAPTION);
      helpSpec.setIndicator(HELP_CAPTION, getResources().getDrawable(R.drawable.icon_log_tab));
      Intent helpIntent = new Intent(this, Help.class);
      helpSpec.setContent(helpIntent);

      TabSpec aboutSpec = tabHost.newTabSpec(ABOUT_CAPTION);
      aboutSpec.setIndicator(ABOUT_CAPTION, getResources().getDrawable(R.drawable.icon_visualise_tab));
      Intent aboutIntent = new Intent(this, About.class);
      aboutSpec.setContent(aboutIntent);
           
      tabHost.addTab(helpSpec);
      tabHost.addTab(aboutSpec);
  }
}