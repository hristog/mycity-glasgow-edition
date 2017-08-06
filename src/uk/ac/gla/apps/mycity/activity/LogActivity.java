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

import uk.ac.gla.apps.mycity.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class LogActivity extends Activity {
	private EditText durationTextView;
	private SeekBar durationSeekBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		durationSeekBar = (SeekBar) findViewById(R.id.logActivity_seekBar_duration);
		durationTextView = (EditText) findViewById(R.id.logActivity_editText_duration);
		final Spinner typeSpinner = (Spinner) findViewById(R.id.logActivity_spinner_type);
		Button logActivityButton = (Button) findViewById(R.id.logActivity_button_log);
		
		durationTextView.setText("0");
		durationSeekBar.setMax(180);
		
		int numActivities = uk.ac.gla.apps.mycity.helper.Activity.CAPTIONS.length-1;
		String[] activityTypes = new String[numActivities];
		for (int i = 0; i < numActivities; i++) {
			activityTypes[i] = uk.ac.gla.apps.mycity.helper.Activity.CAPTIONS[i+1];
		}
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activityTypes);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(spinnerArrayAdapter);

		durationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				durationTextView.setText(String.format("%d", progress));
			}
		});
		
		logActivityButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.logActivity_button_log:
				    int activityTypeId = typeSpinner.getSelectedItemPosition() + 1;
					break;
				}
			}
		});
	}
	
	/*private static int getActivityTypeId(String activity) {
		for (int i = 0; i < uk.ac.gla.apps.mycity.helper.Activity.CAPTIONS.length; i++) {
			if (activity.equals(uk.ac.gla.apps.mycity.helper.Activity.CAPTIONS[i])) {
				return i;
			}
		}
		return -1;
	}*/
}