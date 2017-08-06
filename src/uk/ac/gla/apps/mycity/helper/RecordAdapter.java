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

import java.util.List;

import uk.ac.gla.apps.mycity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecordAdapter extends ArrayAdapter<Record> {
    private List<Record> items;
    private Context context;

    public RecordAdapter(Context context, int textViewResourceId, List<Record> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rankings, null);
        }

        Record item = items.get(position);
        if (item != null) {
            TextView userName = (TextView) view.findViewById(R.id.rankings_userName);
            if (userName != null) {
            	userName.setText(String.format("%d. %s", position+1, item.getDisplayName()));
            }
        
	        TextView overallScore = (TextView) view.findViewById(R.id.rankings_overallScore);
	        if (overallScore != null) {
	        	overallScore.setText(String.valueOf(item.getOverallScore()));
	        }
	        
	        TextView numCastles = (TextView) view.findViewById(R.id.rankings_numCastles);
	        if (numCastles != null) {
	        	numCastles.setText(String.valueOf(item.getNumCastles()));
	        }
	        
	        TextView numProperties = (TextView) view.findViewById(R.id.rankings_numProperties);
	        if (numProperties != null) {
	        	numProperties.setText(String.valueOf(item.getNumProperties()));
	        }
	    }
	    return view;
    }
}