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

package uk.ac.gla.apps.mycity.map;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gla.apps.mycity.account.CreateAccount;
import uk.ac.gla.apps.mycity.account.Menu;
import uk.ac.gla.apps.mycity.account.Welcome;
import uk.ac.gla.apps.mycity.activity.PhysicalActivity;
import uk.ac.gla.apps.mycity.helper.Collectible;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.GameBoard;
import uk.ac.gla.apps.mycity.helper.User;
import uk.ac.gla.apps.mycity.profile.Profile;
import uk.ac.gla.apps.mycity.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MyCity extends MapActivity {
	private MapView mapView;
	private List<Overlay> mapOverlays;
	private MyLocationOverlay myLocationOverlay;
	private CustomItemizedOverlay itemizedOverlay;
	private DatabaseHelper dbHelper;
	private MapHelper mapHelper;
	private User user;
	
	private final static int MY_PROPERTIES = 0;
	private final static int MY_FRIENDS_PROPERTIES = 1;
	private final static int MY_PROPERTIES_AND_MY_FRIENDS_PROPERTIES = 2;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		dbHelper = new DatabaseHelper(this);
			
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setSatellite(false);
		mapView.setBuiltInZoomControls(true);
		
		int mode = -1;
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	mode = extras.getInt("mode");
        	user = extras.getParcelable("user");
        }
        else {
        	throw new NullPointerException("Unexpected call of MyCity activity");
        }
        
        final int currentMode = mode;
        
	    //dbHelper.deleteAllHouses();

        GameBoard gameBoardGlasgowEdition = dbHelper.getGameBoard("Glasgow");
		Bitmap currentLocationBitmap = BitmapFactory.decodeResource(getResources(), user.getTokenId());
		
		mapHelper = MapHelper.getInstance(MyCity.this, currentMode, gameBoardGlasgowEdition.getTopLeft(), gameBoardGlasgowEdition.getBottomRight());
		Log.v("DRAWABLE", String.format("%d/%d", R.drawable.house_default, user.getHouseType()));
		Drawable houseBitmap = getResources().getDrawable(user.getHouseType());
		itemizedOverlay = new CustomItemizedOverlay(this, houseBitmap, mapHelper, dbHelper);		
		
		myLocationOverlay = new MapOverlay(MyCity.this, currentLocationBitmap, mapHelper, mapView);
		//myLocationOverlay = new MyLocationOverlay(MyCity.this, mapView);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		mapOverlays = mapView.getOverlays();
			
		myLocationOverlay.runOnFirstFix(new Runnable() { 
			public void run() {
				animateToCurrentLocation();
				mapOverlays.add(myLocationOverlay);
				mapOverlays.add(itemizedOverlay);
				mapView.postInvalidate();
			}
		});
		
		List<CustomOverlayItem> propertyList;
		
		int propertyViewMode = MY_PROPERTIES_AND_MY_FRIENDS_PROPERTIES;
		
		switch(propertyViewMode) {
		case MY_PROPERTIES:
			propertyList = dbHelper.getPropertiesByUser(user.getId());
			break;
		case MY_FRIENDS_PROPERTIES:
			propertyList = dbHelper.getFriendsPropertiesByUser(user.getId());
			break;
		case MY_PROPERTIES_AND_MY_FRIENDS_PROPERTIES:
			propertyList = dbHelper.getUserAndFriendsPropertiesByUser(user.getId());
			break;
		default: // NO_PROPERTIES 
			propertyList = new ArrayList<CustomOverlayItem>();
		}
		
		List<CustomOverlayItem> parkCardList = dbHelper.getAllCollectiblesOfType(Collectible.PARK_CARD);
		List<CustomOverlayItem> cityCardList = dbHelper.getAllCollectiblesOfType(Collectible.CITY_CARD);
		
		Log.i("PROPERTY_LIST", String.format("%d", propertyList.size()));
		Log.i("PARK_CARD_LIST", String.format("%d", parkCardList.size()));
		Log.i("CITY_CARD_LIST", String.format("%d", cityCardList.size()));
		// TODO: itemizedOverlay.getOverlays().remove(propertyList);
		
		for (CustomOverlayItem i : propertyList) {
			itemizedOverlay.addItem(i);
		}
		
		for (CustomOverlayItem i : parkCardList) {
			itemizedOverlay.addItem(i);
		}
		
		for (CustomOverlayItem i : cityCardList) {
			itemizedOverlay.addItem(i);
		}
		
		SideMenu sideMenu = new SideMenu(this, new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent;
				switch(v.getId()) {
				case(R.id.map_button_current_location):
					animateToCurrentLocation();
					break;
				case(R.id.map_button_build_house):
					mapHelper.toggleBuildMode();
					break;
				case(R.id.map_button_show_profile):
					dbHelper.close();
					intent = new Intent(MyCity.this, Profile.class);
					intent.putExtra("user", user);
			    	startActivityForResult(intent, 0);
					break;
				case(R.id.map_button_exit):
					dbHelper.close();
			    	intent = new Intent(MyCity.this, Menu.class);
					intent.putExtra("user", user);
					startActivityForResult(intent, 0);
					break;
				}
			}
		});
		sideMenu.addMenuOptions(new Integer[] { R.id.map_button_current_location,
										R.id.map_button_build_house,
										R.id.map_button_show_profile,
										R.id.map_button_exit });
	}
	
	protected MyLocationOverlay getMyLocationOverlay() {
		return myLocationOverlay;
	}
	
	protected User getCurrentUser() {
		return user;
	}
	
	private void animateToCurrentLocation() {
		MapController mc = mapView.getController();
		mc.animateTo(myLocationOverlay.getMyLocation());
		mc.setZoom(18);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbHelper.close();
	}
	
	public MapView getMapView() {
		return mapView;
	}
}