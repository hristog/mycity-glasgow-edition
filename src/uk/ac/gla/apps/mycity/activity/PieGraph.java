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

import java.util.List;

import uk.ac.gla.apps.mycity.helper.Activity;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class PieGraph {
	private int[] values;
	private int[] mins;
	private String[] captions;
	private String title;
	
	private final int[] colours = new int[] { Color.RED, Color.GREEN,
			Color.YELLOW, Color.CYAN, Color.BLUE };
	
	public PieGraph(List<Activity> activityList, String title) {
		this.title = title;
		values = new int[Activity.numActivityTypes];
		mins = new int[Activity.numActivityTypes];
		captions = new String[Activity.numActivityTypes];
		int valuesTotal = 0;
		
		for (int i = 0; i < Activity.numActivityTypes; i++) {
			values[i] = 0;
		}
		
		for (Activity a : activityList) {
			int activityType = a.getType();
			if (activityType != Activity.SEDENTARY) {
				values[activityType] += a.getDuration() * Activity.COEFFICIENTS[activityType];
				mins[activityType] += a.getDuration();
				
				valuesTotal += values[activityType];
			}
		}
		
		int valueRecommended = Activity.RECOMMENDED_DURATION * Activity.COEFFICIENTS[Activity.RECOMMENDED];
		int remaining = valueRecommended - valuesTotal;
		values[Activity.RECOMMENDED] = remaining > 0 ? remaining : 0;
		mins[Activity.RECOMMENDED] = remaining / Activity.COEFFICIENTS[Activity.RECOMMENDED];
		
		for (int i = 0; i < Activity.numActivityTypes; i++) {
			float percentage = ((float) values[i] / (float) valueRecommended) * 100;
			captions[i] = String.format("%s (%.2f%%, %d mins)", Activity.CAPTIONS[i], percentage, mins[i]);
		}
	}

	public Intent getIntent(Context context) {
		CategorySeries series = new CategorySeries(title);
		
		for (int i = 0; i < Activity.numActivityTypes; i++) {
			series.add(captions[i], values[i]);
		}

		DefaultRenderer renderer = new DefaultRenderer();
		for (int colour : colours) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colour);
			renderer.addSeriesRenderer(r);
		}
		renderer.setChartTitle(title);
		renderer.setChartTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(26);
		renderer.setZoomButtonsVisible(true);
		
		Intent intent = ChartFactory.getPieChartIntent(context, series, renderer, title);
		return intent;
	}
}
