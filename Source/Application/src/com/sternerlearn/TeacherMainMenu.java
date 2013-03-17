package com.sternerlearn;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class TeacherMainMenu extends MyListActivity {

	public enum BUTTONS
	{
		STUDENTS,
		CLASSES
	}
	
	public String[] myTitles = {
			"Students",
			"Classes"
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TITLES = myTitles;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_main_menu, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		if( position == BUTTONS.STUDENTS.ordinal() )
		{
			startActivity( new Intent(this, NotImplemented.class));
		}
		else if( position == BUTTONS.CLASSES.ordinal() )
		{
			startActivity( new Intent(this, NotImplemented.class));
		}
	}	

}
