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

package uk.ac.gla.apps.mycity.account;

import uk.ac.gla.apps.mycity.R;

import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.User;
import uk.ac.gla.apps.mycity.profile.HouseSelection;
import uk.ac.gla.apps.mycity.profile.TokenSelection;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class LogInToAccount extends Activity {
	protected static final int MODE_TRY = 0;
	protected static final int MODE_FIRST_TIME_USER = 1;
	protected static final int MODE_NORMAL = 2;
	protected static final int SETTINGS = 3;
	
	private static int mode;
	private DatabaseHelper db;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    db = new DatabaseHelper(LogInToAccount.this);
	    
	    Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	mode = extras.getInt("mode");
        }
        else {
        	mode = MODE_TRY;
        }
        
       	setContentView(R.layout.account_log_in);
       	
        Button logInToAccountButton = (Button) findViewById(R.id.logInToAccount_logInAccount);
        Button cancelButton = (Button) findViewById(R.id.logInToAccount_cancel);
        
        final TextView emailTextView = (TextView) findViewById(R.id.logInToAccount_email);
        final TextView passwordTextView = (TextView) findViewById(R.id.logInToAccount_password);
        
	    View.OnClickListener logInToAccountOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = null;
				User user = null;
				String email = emailTextView.getText().toString();
				String password = passwordTextView.getText().toString();
				
				switch(v.getId()) {
				case (R.id.logInToAccount_logInAccount):
					if ((user = db.logIn(email, password)) == null) {
						Toast.makeText(LogInToAccount.this, String.format("We do not have records for any users registered with e-mail %s. Please, create an account if you do not already have one.", email), Toast.LENGTH_SHORT).show();
					}
					else {
						intent = new Intent(LogInToAccount.this, Menu.class);
						intent.putExtra("user", user);
					} 
					intent.putExtra("mode", mode);
					break;
				case (R.id.logInToAccount_cancel):
					intent = new Intent(LogInToAccount.this, Welcome.class);
					startActivityForResult(intent, 0);
					break;
				}

				if (user != null) {
					db.close();
					startActivityForResult(intent, 0);
				}
			}
		};
		
		logInToAccountButton.setOnClickListener(logInToAccountOnClickListener);
		cancelButton.setOnClickListener(logInToAccountOnClickListener);
	}
}