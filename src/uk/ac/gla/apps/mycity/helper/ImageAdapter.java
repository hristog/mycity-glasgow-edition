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

package uk.ac.gla.apps.mycity.helper;

import uk.ac.gla.apps.mycity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int[] charDrawableIds;
    private String[] captions;
    private int layout;
    private boolean showCaptions;
    
    public ImageAdapter(Context context, int[] charDrawableIds, String[] captions, int layout) {
    	this.context = context;
    	this.charDrawableIds = charDrawableIds;
    	this.captions = captions;
    	this.layout = layout;
    	this.showCaptions = true;
    }
    
    public ImageAdapter(Context context, int[] charDrawableIds, String[] captions, int layout, boolean showCaptions) {
    	this(context, charDrawableIds, captions, layout);
    	this.showCaptions = showCaptions;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {  // if it's not recycled, initialise some attributes
        	LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	v = li.inflate(layout, null);
        }
        else {
        	v = convertView;
        }
        
    	if (showCaptions) {
    		TextView tv = (TextView) v.findViewById(R.id.item_text);
    		tv.setText(captions[position]);
    	}
    	ImageView iv = (ImageView) v.findViewById(R.id.item_image);
    	iv.setImageResource(charDrawableIds[position]);
        return v;
    }

	public int getCount() {
		return charDrawableIds.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}