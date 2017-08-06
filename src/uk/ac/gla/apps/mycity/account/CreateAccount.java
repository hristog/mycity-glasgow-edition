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
import uk.ac.gla.apps.mycity.map.MyCity;
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

public class CreateAccount extends Activity {
	protected static final int MODE_TRY = 0;
	protected static final int MODE_FIRST_TIME_USER = 1;
	protected static final int MODE_NORMAL = 2;
	protected static final int SETTINGS = 3;
	
	private int currentTokenPos;
	private int currentHouseTypePos;
	private ImageView currentToken;
	private ImageView currentHouseType;
	
	private static int mode;
	private DatabaseHelper db;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    db = new DatabaseHelper(CreateAccount.this);
	    
	    final int[] tokenIds = TokenSelection.getTokenIds();
        final int[] houseTypeIds = HouseSelection.getHouseTypeIds();
        
	    int tokenId = -1;
	    
	    Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	mode = extras.getInt("mode");
        	if (extras.containsKey("tokenId")) {
        		tokenId = extras.getInt("tokenId");
        	}
        }
        else {
        	mode = MODE_TRY;
        }
        
        final int currentTokenId = tokenId;
        
       	setContentView(R.layout.account_create_new);
       	
        Button chooseTokenAndHouseTypeButton = (Button) findViewById(R.id.createNewAccount_button_chooseTokenAndHouse);
        Button cancelButton = (Button) findViewById(R.id.createNewAccount_cancel);
        
        final TextView emailTextView = (TextView) findViewById(R.id.createNewAccount_email);
        final TextView passwordTextView = (TextView) findViewById(R.id.createNewAccount_password);
        final TextView displayNameTextView = (TextView) findViewById(R.id.createNewAccount_displayName);
        
	    View.OnClickListener createAccountOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = null;
				User user = null;
				String email = emailTextView.getText().toString();
				String password = passwordTextView.getText().toString();
				String displayName = displayNameTextView.getText().toString();
				// Log.i("PROFILE_LOG", String.format("email: %s, password: %s", email, password));
				
				switch(v.getId()) {
				case (R.id.createNewAccount_button_chooseTokenAndHouse):
					int newUserId;
					//if ((newUserId = db.addUser(displayName, email, password, tokenIds[currentTokenPos], houseTypeIds[currentHouseTypePos])) != -1) {
					//	user = new User(newUserId, displayName, email, tokenIds[currentTokenPos], houseTypeIds[currentHouseTypePos]);
						intent = new Intent(CreateAccount.this, TokenSelection.class);
						intent.putExtra("user", user);
					//}
					//else {
					//	Toast.makeText(CreateAccount.this, String.format("A user with e-mail %s has already registered with our system. Please, choose another e-mail for your account.", email), Toast.LENGTH_SHORT).show();
					//}
					break;
				case (R.id.createNewAccount_cancel):
					intent = (currentTokenId == -1) ? new Intent(CreateAccount.this, Welcome.class) : new Intent(CreateAccount.this, MyCity.class);
					intent.putExtra("mode", mode);
					startActivityForResult(intent, 0);
					break;
				}
				
				db.close();
				intent.putExtra("mode", mode);
				startActivityForResult(intent, 0);

			}
		};
		
		chooseTokenAndHouseTypeButton.setOnClickListener(createAccountOnClickListener);
		cancelButton.setOnClickListener(createAccountOnClickListener);
	}
}