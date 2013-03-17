package com.sternerlearn;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AssignmentsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignments);
		
		// TODO: Need to basically make my own CalendarView to support what we need
		// Want to sorta mimic the Google calendar view
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignments, menu);
		return true;
	}

}
