package com.sternerlearn;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;


public class ParentMainMenu extends MyListActivity {
	
	private enum BUTTONS
	{
		LINK_ACCOUNT,
		CLASSES,
		INFRACTIONS,
		PARENTAL_CONTROLS
	}
	
	private String[] myTitles = {
			"Link Accounts",
			"Classes",
			"Infractions",
			"Parental Controls"
	};		

	@Override
	// TODO: Need to be able to disable/remove list items when they are not valid
	protected void onCreate(Bundle savedInstanceState) {
		TITLES = myTitles;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		if( position == BUTTONS.LINK_ACCOUNT.ordinal() )
		{
			startActivity( new Intent(this, LinkAccountsActivity.class));
		}
		else if( position == BUTTONS.CLASSES.ordinal() )
		{
			startActivity( new Intent(this, ClassesActivity.class));
		}
		else if( position == BUTTONS.INFRACTIONS.ordinal() )
		{
			startActivity( new Intent(this, InfractionsActivity.class));
		}
		else if( position == BUTTONS.PARENTAL_CONTROLS.ordinal() )
		{
			startActivity( new Intent(this, ParentalControlActivity.class));
		}
	}

}
