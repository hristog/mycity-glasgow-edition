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

import java.security.GeneralSecurityException;

import uk.ac.gla.apps.mycity.R;
import uk.ac.gla.apps.mycity.helper.DatabaseHelper;
import uk.ac.gla.apps.mycity.helper.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome extends Activity {
	DatabaseHelper dbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	    
	    dbHelper = new DatabaseHelper(Welcome.this);
	    
	    if (dbHelper.referencesNotUpdated()) {
	    	dbHelper.updateAllCollectibleDrawablesReferences();
	    }
	    
	    Button debug = (Button) findViewById(R.id.button_debug);
	    //Button tryApp = (Button) findViewById(R.id.button_tryApp);
	    //Button createAccount = (Button) findViewById(R.id.button_createAccount);
	    //Button logIn = (Button) findViewById(R.id.button_logIn);
	    
	    View.OnClickListener welcomeListener = new View.OnClickListener() {
			public void onClick(View v) {
				Intent menu = null;
				switch(v.getId()) {
				case (R.id.button_debug):
					//menu = new Intent(Welcome.this, Menu.class);
					//menu.putExtra("mode", CreateAccount.MODE_TRY);
					User user = null;
					//menu.putExtra("user", new User(28713231, "User 8173", "user8173@email.com"));
					String email = "user1@email.com", pass = "1234";
					if ((user = dbHelper.logIn(email, pass)) == null) {
						Toast.makeText(Welcome.this, String.format("Either such account does not exist or the e-mail and password entered do not match. Please, create an account if you do not already have one.", email), Toast.LENGTH_SHORT).show();
					}
					else {
						user.setTokenId(R.drawable.token_9);
						user.setHouseType(R.drawable.house_default);
						user.setMyCityPoints(5000);
						menu = new Intent(Welcome.this, Menu.class);
						menu.putExtra("mode", CreateAccount.MODE_TRY);
						menu.putExtra("user", user);
						dbHelper.close();
					}
					break;
				/* case (R.id.button_tryApp):
					//menu = new Intent(Welcome.this, TokenSelection.class);
					//menu.putExtra("mode", Account.MODE_TRY);
					break;
				case (R.id.button_createAccount):
					menu = new Intent(Welcome.this, CreateAccount.class);
	        		menu.putExtra("mode", CreateAccount.MODE_FIRST_TIME_USER);
					break;
				case (R.id.button_logIn):
					menu = new Intent(Welcome.this, LogInToAccount.class);
					menu.putExtra("mode", CreateAccount.MODE_NORMAL);
					break; */
				}
				if (menu != null) {
					startActivityForResult(menu, 0);
				}
			}
		};
		
		debug.setOnClickListener(welcomeListener);
		//tryApp.setOnClickListener(welcomeListener);
		//createAccount.setOnClickListener(welcomeListener);
		//logIn.setOnClickListener(welcomeListener);
	}
}