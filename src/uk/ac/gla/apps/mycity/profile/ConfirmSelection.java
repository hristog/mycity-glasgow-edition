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

package uk.ac.gla.apps.mycity.profile;

import uk.ac.gla.apps.mycity.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ConfirmSelection extends Activity {
	private int tokenId;
	private int houseTypeId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.selection);
	    
	    Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	tokenId = extras.getInt("tokenId");
        	houseTypeId = extras.getInt("houseTypeId");
        }
        else {
        	tokenId = R.drawable.token_default;
        	houseTypeId = R.drawable.house_default;
        }
        
        ImageView token = (ImageView) findViewById(R.id.selection_token);
        ImageView houseType = (ImageView) findViewById(R.id.selection_house);
        
        token.setImageDrawable(this.getResources().getDrawable(tokenId));
        houseType.setImageDrawable(this.getResources().getDrawable(houseTypeId));
	}
}