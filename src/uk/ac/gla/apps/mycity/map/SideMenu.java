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

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

public class SideMenu {
	private List<ImageButton> buttons;
	private Context context;
	private View.OnClickListener sideMenuListener;
	
	public SideMenu(Context c, View.OnClickListener l) {
		buttons = new ArrayList<ImageButton>();
		context = c;
		sideMenuListener = l;
	}
	
	public void addMenuOption(int id) {
		if (sideMenuListener != null) {
			ImageButton ib = (ImageButton) ((Activity) context).findViewById(id);
			//ib.setImageDrawable(MapHelper.scaleDrawable(ib.getDrawable(), 32, 32));
			ib.setImageDrawable(ib.getDrawable());
			ib.setOnClickListener(sideMenuListener);
			buttons.add(ib);
		}
		else {
			throw new NullPointerException("Please, set the side menu OnClickListener before adding any new menu options!");
		}
	}
	
	public void addMenuOptions(Integer[] ids) {
		int numMenuOptions = ids.length;
		for (int i = 0; i < numMenuOptions; i++) {
			addMenuOption(ids[i]);
		}
	}
	
	public void setOnClickListener(View.OnClickListener listener) {
		sideMenuListener = listener;
	}
}