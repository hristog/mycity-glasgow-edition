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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapOverlay extends MyLocationOverlay {
	private Bitmap bitmap;
	private MapHelper mapHelper;
	
	private Point tokenCoords;
	private Point currentPoint;
	private float side;
	private int lastElemY;

	public MapOverlay(Context c, Bitmap b, MapHelper mh, MapView mv) {
		super(c, mv);
		// bitmap = MapHelper.scaleBitmap(b, 64, (int) (((float) b.getHeight() / (float) b.getWidth()) * 64.0));
		bitmap = b;
		mapHelper = mh;
		lastElemY = 0;
		tokenCoords = new Point();
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
		super.draw(canvas, mapView, shadow, when);
		Paint paint = new Paint();
		Paint paint2 = new Paint();

		mapHelper.updateProjection(mapView.getProjection());
		Point topLeft = mapHelper.getTopLeft();
		side = mapHelper.getSide();

		currentPoint = MapHelper.getCurrentPoint();
		tokenCoords.x = currentPoint.x - bitmap.getWidth() / 2;
		tokenCoords.y = currentPoint.y - bitmap.getHeight() - 20;
		
		// String pointCoordsStr = String.format("%d, %d", currentPoint.x, currentPoint.y);
		// String tokenCoordsStr = String.format("%d, %d", tokenCoords.x, tokenCoords.y);
		
		paint2.setStrokeWidth(1);
		paint2.setARGB(255, 0, 0, 0);
		paint2.setStyle(Paint.Style.FILL);
		paint2.setAntiAlias(true);
		paint2.setTextSize(30);

		setLastElemY(mapView.getTop() + 135);
		
		final int paddingLeft = mapView.getLeft() + 25;
		
		canvas.drawBitmap(bitmap, tokenCoords.x, tokenCoords.y, paint2);
		//canvas.drawBitmap(bitmap, tokenCoords.x - (bitmap.getWidth() / 2), tokenCoords.y - (bitmap.getHeight() + 20), paint2);
		canvas.drawText("MyCity Points: " + mapHelper.getMyCityPoints(), paddingLeft, getLastElemY(), paint2);
		canvas.drawText("Score: " + mapHelper.getOverallScore(), paddingLeft, getNewLastElemY(), paint2);
		
		if (mapHelper.isBuildModeOn()) {// && mapHelper.getMode() != MapHelper.MODE_TRY) { // && MapView.getZoomLevel() > 18) {
			canvas.drawText("Building mode is ON", mapView.getLeft() + 25, lastElemY + 50, paint2);
			
			paint.setDither(true);
			paint.setColor(Color.GRAY);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(1);
			paint.setPathEffect(new DashPathEffect(new float[] { 10, 10 }, 0));

			float offsetX;
			float offsetY = topLeft.y;

			int zoneDensity = MapHelper.getZoneDensity();

			for (int y = 0; y < zoneDensity; y++) {
				offsetX = topLeft.x;

				for (int x = 0; x < zoneDensity; x++) {
					if (isWithinScope(mapView, currentPoint, offsetX, offsetY, side)) {
						// Log.i("LOG", "[" + y + ", " + x + "] offsetX = " + offsetX + ", offsetY = " + offsetY);
						canvas.drawRect(offsetX, offsetY, offsetX + side, offsetY + side, paint);
					}
					offsetX += side;
				}
				offsetY += side;
			}
		}		
		/* canvas.drawText("Current mode: " + mapHelper.getMode(), mapView.getLeft() + 25, lastElemY, paint2);
		canvas.drawText("Building type: " + mapHelper.getHouseType(), mapView.getLeft() + 25, lastElemY + 25, paint2);
		canvas.drawText("Point coordinates: " + pointCoordsStr, mapView.getLeft() + 25, lastElemY + 50, paint2);
		canvas.drawText("Token coordinates: " + tokenCoordsStr, mapView.getLeft() + 25, lastElemY + 75, paint2);
		canvas.drawText("Side: " + side, mapView.getLeft() + 25, lastElemY + 100, paint2); */
		// canvas.drawText("Current address: " + address, mapView.getLeft() + 25, lastElemY + 150, paint2);

		return true;
	}
	
	/* Override drawCompass();
	Manipulate the Canvas object you're handed as necessary.
	Call super.drawCompass() to draw the compass at your new location.
	Restore the Canvas to how you found it.
	Profit. */
	
	@Override
	public void drawCompass(Canvas canvas, float bearing) {
		super.drawCompass(canvas, bearing);
	}
	
	private boolean isWithinScope(View view, Point p, float offsetX, float offsetY, float side) {
		float screenWidth = view.getWidth();
		float screenHeight = view.getHeight();

		if (offsetX + side < 0 || offsetX > screenWidth || offsetY + side < 0
				|| offsetY > screenHeight) {
			return false;
		}

		if (offsetX < p.x - 2 * side || offsetX > p.x + side
				|| offsetY < p.y - 2 * side || offsetY > p.y + side) {
			return false;
		}
		return true;
	}
	
	private boolean isWithinSameBlock(View view, Point p, float offsetX, float offsetY, float side) {
		float screenWidth = view.getWidth();
		float screenHeight = view.getHeight();

		if (offsetX + side < 0 || offsetX > screenWidth || offsetY + side < 0
				|| offsetY > screenHeight) {
			return false;
		}

		if (offsetX < p.x - side || offsetX > p.x + side
				|| offsetY < p.y - side|| offsetY > p.y) {
			return false;
		}
		return true;
	}
	
	public boolean isInCloseProximity(Point p, float x, float y) {
		 return x > p.x - 10 && x < p.x + bitmap.getWidth() + 10 && 
				 y > p.y - 10 && y < p.y + bitmap.getHeight() + 10;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		/*float x = event.getX();
		float y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.i("PRESS_EVENT", String.format("[%f, %f]", x, y), Toast.LENGTH_SHORT).show();
		}*/
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		mapHelper.updateView();
	}
	
	public void setLastElemY(int lastElemY) {
		this.lastElemY = lastElemY;
	}
	
	public int getNewLastElemY() {
		lastElemY += 25;
		return lastElemY;
	}
	
	public int getLastElemY() {
		return lastElemY;
	}
}