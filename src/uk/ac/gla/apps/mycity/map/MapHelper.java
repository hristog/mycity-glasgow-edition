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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import uk.ac.gla.apps.mycity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

public class MapHelper {
	private int mode;
	private static android.app.Activity activity;
	
	private boolean buildModeOn;
	
	protected final static int MODE_TRY = 0;
	protected final static int MODE_FIRST_TIME_USER = 1;
	protected final static int MODE_NORMAL = 2;
	
	private static final int zoneDensity = 500;
	private String provider;
	private static Projection projection;
	
	private GeoPoint topLeftRegionGeoPoint;
	private GeoPoint bottomRightRegionGeoPoint;
	private Point topLeft;
	private Point bottomRight;
	private float side;
	
	private static MapHelper instance = null;
	
	public static MapHelper getInstance(android.app.Activity activity, int mode, GeoPoint topLeftRegionGeoPoint, GeoPoint bottomRightRegionGeoPoint) {
		if(instance == null) {
			instance = new MapHelper(activity, mode, topLeftRegionGeoPoint, bottomRightRegionGeoPoint);
		}
	    return instance;
	}
		
	private MapHelper(android.app.Activity a, int mode, GeoPoint topLeftRegionGeoPoint, GeoPoint bottomRightRegionGeoPoint) {
		this.mode = mode;
		activity = a;
		projection = null;
		buildModeOn = false;
		this.topLeftRegionGeoPoint = topLeftRegionGeoPoint;
		this.bottomRightRegionGeoPoint = bottomRightRegionGeoPoint;
    }
	
	public int getMode() {
		return mode;
	}
	
	public void toggleBuildMode() {
		buildModeOn = !buildModeOn;
	}

	public int getHouseType() {
		return ((MyCity) activity).getCurrentUser().getHouseType();
	}
	
	public int getUserId() {
		return ((MyCity) activity).getCurrentUser().getId();
	}
	
	public String getUserName() {
		return ((MyCity) activity).getCurrentUser().getDisplayName();
	}
	
	public int getMyCityPoints() {
		return ((MyCity) activity).getCurrentUser().getMyCityPoints();
	}
	
	public void increaseMyCityPoints(int amount) {
		((MyCity) activity).getCurrentUser().increaseMyCityPoints(amount);
		((MyCity) activity).getCurrentUser().increaseOverallScore(amount);
	}
	
	public void decreaseMyCityPoints(int amount, int propertyType) {
		((MyCity) activity).getCurrentUser().decreaseMyCityPoints(amount);
		switch (propertyType) {
		case 0: // HOUSE
			((MyCity) activity).getCurrentUser().increaseOverallScore(amount);
			break;
		case 1: // CASTLE
			((MyCity) activity).getCurrentUser().increaseOverallScore(amount * 2);
			break;
		}
	}
	
	public int getOverallScore() {
		return ((MyCity) activity).getCurrentUser().getOverallScore();
	}
	
	public static int getZoneDensity() {
		return zoneDensity;
	}
	
	public boolean isBuildModeOn() {
		return buildModeOn;
	}
		
	public static GeoPoint geoPointFromLocation(Location location) {
		return new GeoPoint((int) (location.getLatitude() * 1000000), (int) (location.getLongitude() * 1000000));
	}
	
	public static Point pointFromLocation(Location location) {
		Point point = new Point();
		getProjection().toPixels(geoPointFromLocation(location), point);
		return point;
	}
	
	public static Point pointFromGeoPoint(GeoPoint geoPoint) {
		Point point = new Point();
		getProjection().toPixels(geoPoint, point);
		return point;
	}
	
	public static Drawable scaleResource(int resourceId, int newWidth, int newHeight) {
		Drawable drawable = activity.getResources().getDrawable(resourceId);
		return scaleDrawable(drawable, newWidth, newHeight);
	}
	
	public static Drawable getDrawable(int resourseId) {
		Drawable drawable = activity.getResources().getDrawable(resourseId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		return drawable;
	}
	
	public static Drawable scaleDrawable(Drawable drawable, int newWidth, int newHeight) {
		Bitmap bb = ((BitmapDrawable) drawable).getBitmap();

		int width = bb.getWidth();
		int height = bb.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resultBitmap = Bitmap.createBitmap(bb, 0, 0, width, height, matrix, true);
		return drawable = new BitmapDrawable(resultBitmap);
	}
	
	/* public static void setOverlayItemMarker(OverlayItem item, int drawableId) {
		Drawable house = MapHelper.scaleResource(drawableId, 64, 64);
		item.setMarker(house);
		Log.i("LOG", "Marker set to [house_red]");
	} */

	/* public static Drawable scaleResource(Activity a, int resourceId, int newWidth, int newHeight) {
		Drawable drawable = a.getResources().getDrawable(resourceId);
		Bitmap bb = ((BitmapDrawable) drawable).getBitmap();

		int width = bb.getWidth();
		int height = bb.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
	
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resultBitmap = Bitmap.createBitmap(bb, 0, 0, width, height, matrix, true);
		return drawable = new BitmapDrawable(resultBitmap);
	} */
	
	public static void showDialog(Context context, String title, String message) { 
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNeutralButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		dialog.show();
	}
	
	public static void showDialog(Context context, String title, String message, int buttonTextId, android.content.DialogInterface.OnClickListener listener) { 
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNeutralButton(buttonTextId, listener);
		dialog.show();
	}

	public static Bitmap scaleBitmap(Bitmap bb, int newWidth, int newHeight) {
		int width = bb.getWidth();
		int height = bb.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bb, 0, 0, width, height, matrix, true);
	}
	
	public int xCoordInGrid(Point point) {
		return (int) (Math.abs(point.x - topLeft.x) / side);
	}
	
	public int yCoordInGrid(Point point) {
		return (int) (Math.abs(point.y - topLeft.y) / side);
	}

	public static String getAddress(Context context, Location location) {
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		String strCompleteAddress = "";
		try {
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

			if (addresses.size() > 0) {
				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
					strCompleteAddress += addresses.get(0).getAddressLine(i) + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strCompleteAddress;
	}
	
	/************ Orientation **********************************/
	public static Point getCurrentPoint() {
		/*if (myLocationOverlay == null) { // TODO: (remove) DEBUG
			throw new NullPointerException("myLocationOverlay is NULL!");
		}
		
		if (myLocationOverlay.getMyLocation() == null) { // TODO: (remove) DEBUG
			throw new NullPointerException("myLocationOverlay.getMyLocation() is NULL!");
		}*/
		return pointFromGeoPoint(getCurrentGeoPoint());
	}
	
	public static GeoPoint getCurrentGeoPoint() {
		return ((MyCity) activity).getMyLocationOverlay().getMyLocation();
	}
	
	public Point getTopLeft() {
		return topLeft;
	}
	
	public Point getBottomRight() {
		return bottomRight;
	}
	
	public static Projection getProjection() {
		return projection;
	}
	
	/* public Location getCurrentLocation() {
		return currentLocation;
	} */
	
	public String getProvider() {
		return provider;
	}
	
	public String getCurrentAddress() {
		/* Log.i("LOG", "Current location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
		return MapHelper.getAddress(context, currentLocation); */
		return "";
	}
	
	public void updateProjection(Projection p) {
		projection = p;
		topLeft = pointFromGeoPoint(topLeftRegionGeoPoint);
		bottomRight = pointFromGeoPoint(bottomRightRegionGeoPoint);
		side = (bottomRight.x - topLeft.x) / (float) getZoneDensity();
	}
	
	public float getSide() {
		return side;
	}
	
	public void updateView() { // TODO: REMOVE
		((MyCity) activity).getMapView().invalidate();
	}

	public static GeoPoint geoPointFromPoint(Point point) {
		return projection.fromPixels(point.x, point.y); 
	}
}