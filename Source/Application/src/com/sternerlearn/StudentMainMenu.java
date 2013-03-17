package com.sternerlearn;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class StudentMainMenu extends MyListActivity {

	private enum BUTTONS
	{
		CLASSES,
		ASSIGNMENTS
	}
	
	private String[] myTitles = {
			"Classes",
			"Assignments"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TITLES = myTitles;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_main_menu, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		if( position == BUTTONS.CLASSES.ordinal() )
		{
			startActivity( new Intent(this, ClassesActivity.class));
		}
		else if( position == BUTTONS.ASSIGNMENTS.ordinal() )
		{
			startActivity( new Intent(this, AssignmentsActivity.class));
		}
	}	

}