package com.sternerlearn;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ModifyClassActivity extends MyListActivity {

	private enum BUTTONS
	{
		INFRACTION,
		STUDENT,
		ASSIGNMENT,
		GRADE
	}
	
	private String[] myTitles = {
			"Add Infraction",
			"Add Student",
			"Add Assignment",
			"Assign Grade"
	};
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TITLES = myTitles;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modify_class, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		if( position == BUTTONS.INFRACTION.ordinal() )
		{
			startActivity( new Intent(this, AddInfractionActivity.class));
		}
		else if( position == BUTTONS.STUDENT.ordinal() )
		{
			startActivity( new Intent(this, AddStudentToClassActivity.class));
		}
		else if( position == BUTTONS.ASSIGNMENT.ordinal() )
		{
			startActivity( new Intent(this, AddAssignmentActivity.class));
		}
		else if( position == BUTTONS.GRADE.ordinal() )
		{
			startActivity( new Intent(this, AddGradeActivity.class));
		}		
	}	

}
