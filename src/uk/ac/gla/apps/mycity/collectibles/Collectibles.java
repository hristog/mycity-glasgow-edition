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

import java.util.List;

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.helper.Collectible;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.User;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Collectibles extends TabActivity {
	private DatabaseHelper dbHelper;
	private List<Collectible> myCollectibles;
	private List<Collectible> notYetCollected;

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
      myCollectibles = dbHelper.getUserCollectedItems(userId);
      notYetCollected = dbHelper.getUserNotYetCollectedItems(userId);
		
      dbHelper.close();
      
      final String MY_COLLECTED_ITEMS_CAPTION = String.format("My Collected Items (%d)", myCollectibles.size());
      final String NOT_COLLECTED_YET_CAPTION = String.format("Not Collected Yet (%d)", notYetCollected.size());
      
      TabHost tabHost = getTabHost();

      TabSpec myCollectiblesSpec = tabHost.newTabSpec(MY_COLLECTED_ITEMS_CAPTION);
      myCollectiblesSpec.setIndicator(MY_COLLECTED_ITEMS_CAPTION, getResources().getDrawable(R.drawable.icon_log_tab));
      Intent myCollectiblesIntent = new Intent(this, CollectibleList.class);
      myCollectiblesIntent.putExtra("collectible_drawable_ids", Collectible.getDrawableIds(myCollectibles));
      myCollectiblesIntent.putExtra("collectible_names", Collectible.getNames(myCollectibles));
      myCollectiblesIntent.putExtra("collectible_descriptions", Collectible.getDescriptions(myCollectibles));
      myCollectiblesIntent.putExtra("user", user);
      myCollectiblesSpec.setContent(myCollectiblesIntent);
      
      TabSpec notCollectedYetSpec = tabHost.newTabSpec(NOT_COLLECTED_YET_CAPTION);
      notCollectedYetSpec.setIndicator(NOT_COLLECTED_YET_CAPTION, getResources().getDrawable(R.drawable.icon_summary_tab));
      Intent notCollectedYetIntent = new Intent(this, CollectibleList.class);
      notCollectedYetIntent.putExtra("collectible_drawable_ids", Collectible.getDrawableIds(notYetCollected));
      notCollectedYetIntent.putExtra("collectible_names", Collectible.getNames(notYetCollected));
      notCollectedYetIntent.putExtra("collectible_descriptions", Collectible.getDescriptions(notYetCollected));
      notCollectedYetIntent.putExtra("user", user);
      notCollectedYetSpec.setContent(notCollectedYetIntent);

      tabHost.addTab(myCollectiblesSpec);
      tabHost.addTab(notCollectedYetSpec);
  }
}