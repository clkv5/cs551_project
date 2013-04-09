package com.sternerlearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ParentalControlActivity extends MyListActivity {

	private enum BUTTONS
	{
		MESSAGES,
		LOCATIONS
	}
	
	private String[] myTitles = {
		"View Messages",
		"View Locations"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TITLES = myTitles;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parental_control);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parental_control, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		if( position == BUTTONS.MESSAGES.ordinal() )
		{
			startActivity( new Intent(this, MessagesActivity.class));
		}
		else if( position == BUTTONS.LOCATIONS.ordinal() )
		{
			startActivity( new Intent(this, LocationsActivity.class));
		}
	}

}

