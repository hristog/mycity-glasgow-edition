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

import uk.ac.gla.apps.mycity.helper.Collectible;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.ParkCard;
import uk.ac.gla.apps.mycity.map.CustomOverlayItem;
import uk.ac.gla.apps.mycity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

@SuppressWarnings("rawtypes")
public class CustomItemizedOverlay extends ItemizedOverlay {
	private ArrayList<CustomOverlayItem> mOverlays;
	private Context mContext;
	private MapHelper mapHelper;
	private String provider;
	private DatabaseHelper dbHelper;

	public static final int HOUSE = 0;
	// public static final int CASTLE = 1;
	public static final int PARK_CARD = Collectible.PARK_CARD;
	public static final int CITY_CARD = Collectible.CITY_CARD;
	public static final int CUSTOMISATION_CARD = Collectible.CUSTOMISATION_CARD;
	
	private static final int[] BUILDING_COST = { 500, 5000 };

	public CustomItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		mOverlays = new ArrayList<CustomOverlayItem>();
	}

	public CustomItemizedOverlay(Context context, Drawable defaultMarker, MapHelper mh, DatabaseHelper dbh) {
		this(defaultMarker);
		mContext = context;
		mapHelper = mh;
		provider = mapHelper.getProvider();
		dbHelper = dbh;
		populate();
		
		String dialogTitle = null;
		String dialogMessage = null;
		
		switch(mapHelper.getMode()) {
		case MapHelper.MODE_TRY:
			dialogTitle = "Demo mode";
			dialogMessage = "Please, note that you are currently running the app in demo mode. Not all features of the final version of the app may be available.";
			break;
		case MapHelper.MODE_FIRST_TIME_USER:
			dialogTitle = "Drop off your token";
			dialogMessage = "Please, select the location where you would like to position your token and begin your game from";
			break;
		}
		
		if (dialogTitle != null && dialogMessage != null) {
			MapHelper.showDialog(mContext, dialogTitle, dialogMessage);
		}
	}

	public void addItem(GeoPoint p, String title, String description, long id, int x_grid, int y_grid, int type, int quantity) {
		CustomOverlayItem item = new CustomOverlayItem(p, title, description, id, x_grid, y_grid, type, quantity);
		mOverlays.add(item);
		populate();
	}
	
	public void addItem(GeoPoint p, String title, String description, long id, int x_grid, int y_grid, int type, int quantity, int drawableId) {
		CustomOverlayItem item = new CustomOverlayItem(p, title, description, id, x_grid, y_grid, quantity, type);
		Drawable drawable = MapHelper.getDrawable(drawableId);
		item.setMarker(drawable);
		mOverlays.add(item);
		populate();
	}

	public void addItem(CustomOverlayItem item) {
		mOverlays.add(item);
		populate();
	}

	@Override
	protected CustomOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	public boolean onTap(int index) {
		final CustomOverlayItem item = mOverlays.get(index);
		final int userId = mapHelper.getUserId();
		
		switch(item.getType()) {
		case HOUSE:
			MapHelper.showDialog(mContext, item.getTitle(), item.getSnippet());
			break;
		case PARK_CARD:
			if (!dbHelper.itemHasBeenCollected(userId, item.getId())) {
				dbHelper.collectItem(userId, item.getId());
				MapHelper.showDialog(mContext, item.getTitle(), item.getSnippet(), R.string.label_use_card, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ParkCard cardDrawn = dbHelper.drawParkCard(userId, item.getId());
						if (cardDrawn == null && dbHelper.renewItem(userId, item.getId())) { // item renewed successfully -> draw card
							cardDrawn = dbHelper.drawParkCard(userId, item.getId());
						}
						
						if (cardDrawn != null) { // card is usable
							String message = "";
							switch(cardDrawn.getType()) {
							case ParkCard.MY_CITY_POINT_CARD:
								int amount = cardDrawn.getReference();
								message = String.format("Congratulations! You have won %d MyCity points.", amount);
								mapHelper.increaseMyCityPoints(amount);
								dbHelper.updateUserMyCityPointsAndScore(userId, mapHelper.getMyCityPoints(), mapHelper.getOverallScore());
								// TODO: add card to collectibles
								break;
							}
							Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
							Toast.makeText(mContext, "You have now used this park card.", Toast.LENGTH_SHORT).show();
						}
						else { // not renewable at the moment -> not usable
							Toast.makeText(mContext, "The earliest you can renew this park card is tomorrow.", Toast.LENGTH_SHORT).show();
						}
					}
				});
				Toast.makeText(mContext, "This card has been added to your collection", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(mContext, "You have already got this item in your collection", Toast.LENGTH_SHORT).show();
			}
			break;
		case CITY_CARD:
			if (!dbHelper.itemHasBeenCollected(userId, item.getId())) {
				dbHelper.collectItem(userId, item.getId());
				if (dbHelper.collectItem(mapHelper.getUserId(), item.getId())) {
					MapHelper.showDialog(mContext, item.getTitle(), item.getSnippet());
					Toast.makeText(mContext, "This card has been added to your collection", Toast.LENGTH_SHORT).show();	
				}
			}
			else {
				Toast.makeText(mContext, "You have already got this item in your collection", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return true;
	}
	
	public List<CustomOverlayItem> getOverlays() {
		return mOverlays;
	}

	@Override
	public boolean onTap(final GeoPoint p, final MapView mapView) {
		if (super.onTap(p, mapView)) {
			return true;
		}

		if (mapHelper.isBuildModeOn()) {			
			Point tapped = MapHelper.pointFromGeoPoint(p);
			Point current = MapHelper.getCurrentPoint();

			int x = mapHelper.xCoordInGrid(tapped);
			int y = mapHelper.yCoordInGrid(tapped);
			int xCur = mapHelper.xCoordInGrid(current);
			int yCur = mapHelper.yCoordInGrid(current);

			if (Math.abs(x - xCur) > 1 || Math.abs(y - yCur) > 1) {
				Toast.makeText(mContext,
						"This building block is out of scope",
						Toast.LENGTH_SHORT).show();
				Log.i("LOG", String.format("cur[%d, %d] - p[%d, %d]", xCur,
						yCur, x, y));
			} else {
				if (dbHelper.isOccupied(x, y)) {
					Toast.makeText(mContext,
							"A property has already been built in this block",
							Toast.LENGTH_SHORT).show();
				} else if (mapHelper.getMyCityPoints() < BUILDING_COST[HOUSE]) {
					Toast.makeText(mContext,
							"You do not have enough MyCity points to build a new property",
							Toast.LENGTH_SHORT).show();
				}
				else { // block is free; go ahead and build a new house
					Location location = new Location(provider);
					location.setLatitude(p.getLatitudeE6() / 1E6);
					location.setLongitude(p.getLongitudeE6() / 1E6);

					String title = String.format("%s's property", mapHelper.getUserName());
					String description = String.format("%s", MapHelper.getAddress(mContext, location));
					
					long propertyId = dbHelper.addProperty(p.getLatitudeE6(), p.getLongitudeE6(), x, y, mapHelper.getHouseType(), title, description, mapHelper.getUserId());
					if (propertyId > 0) {
						Toast.makeText(mContext,
								String.format("The building cost for this property was %d MyCity points", BUILDING_COST[HOUSE]),
								Toast.LENGTH_SHORT).show();
						mapHelper.decreaseMyCityPoints(BUILDING_COST[HOUSE], HOUSE);
						dbHelper.updateUserMyCityPointsAndScore(mapHelper.getUserId(), mapHelper.getMyCityPoints(), mapHelper.getOverallScore());
						this.addItem(p, title, description, propertyId, x, y, HOUSE, 1);
						Log.i("LOG", String.format("New property of type [%d] added at location (%d, %d): owner is (%d)", mapHelper.getHouseType(), x, y, mapHelper.getUserId()));
						mapHelper.toggleBuildMode();
					}
				}
			}
		}
		/* else {
			MapHelper.showDialog(mContext, "New GeoPoint", String.format("%d, %d", p.getLatitudeE6(), p.getLongitudeE6()));
			Log.i("GEO_POINT", String.format("Coords: %d, %d", p.getLatitudeE6(), p.getLongitudeE6()));
		} */
		return true;
	}
}