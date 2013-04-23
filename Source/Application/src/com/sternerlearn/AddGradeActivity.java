package com.sternerlearn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

public class AddGradeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_grade);
		
		// Don't show the calendar view
		DatePicker datePicker = (DatePicker)findViewById( R.id.datePicker1 );
		datePicker.setCalendarViewShown(false);
		
		NumberPicker numberPicker = (NumberPicker)findViewById( R.id.numberPicker1 );
		numberPicker.setMinValue( 0 );
		numberPicker.setMaxValue( 1000 );
		numberPicker.setWrapSelectorWheel( false );		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_grade, menu);
		return true;
	}
	
    public void handleSubmit
		(
		View aView
		)
	{
		AsyncCall task = new AsyncCall();
		task.execute();
	} 	
    
    public void addGrade()
    {
    	// TODO: The methods we have for them to select users/assignments is really bad
    	// Improve it...but probably not in the scope for this project
    	
    	Course c = SharedData.getInstance().mCurrentCourse;
    	Account a = SharedData.getInstance().getAccount();
    	
    	String studentName = ((TextView)findViewById(R.id.student_name)).getText().toString();
    	String assignName = ((TextView)findViewById(R.id.assign_name)).getText().toString();
    	
    	ArrayList<Assignment> assignments = Assignment.getAssignments(c);
    	
    	int assignID = -1;
    	for( Assignment assign : assignments )
    	{
    		if( assign.mName.compareTo(assignName) == 0 )
    		{
    			assignID = assign.mId;
    			break;
    		}
    	}
    	
    	// TODO: This doesn't seem to be working
    	if( assignID > 0 )
    	{
	    	// Try to get the student ID
	    	int studentID = -1;
			try
			{
				List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
				properties.add(new PropertyWrapper("aTeacherID", a.mId));
				properties.add(new PropertyWrapper("aPassword", a.mPassword));
				properties.add(new PropertyWrapper("aStudentName", studentName));
				
				SoapSerializationEnvelope env = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_STUDENT, properties);
				
				studentID = java.lang.Integer.parseInt( ((SoapObject)env.getResponse()).toString() );
				
			}
			catch( Exception ex ) {}
	    	
			// TODO: error handling
	    	if( studentID > 0 )
	    	{
	        	NumberPicker picker = (NumberPicker)findViewById(R.id.numberPicker1);
	        	double val = (double)picker.getValue();
	        	
	    		// Selected date
	    		DatePicker datePicker = (DatePicker)findViewById( R.id.datePicker1 );
	    		int day = datePicker.getDayOfMonth();
	    		int month = datePicker.getMonth() + 1;
	    		int year = datePicker.getYear();
	    		
	    		// Convert input to timestamp
	    		String timeString = MessagesActivity.convertToTimestamp(year, month, day, 0, 0, 0);    		        	
	    		
	        	// TODO: Fix the NumberPicker and add the date
				List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
				properties.add(new PropertyWrapper("aTeacherID", a.mId));
				properties.add(new PropertyWrapper("aPassword", a.mPassword));
				properties.add(new PropertyWrapper("aStudentID", studentID));
				properties.add(new PropertyWrapper("aAssignmentID", assignID));
				properties.add(new PropertyWrapper("aPointsReceived", val));
				properties.add(new PropertyWrapper("aDateSubmitted", timeString));
				
				WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_ADD_GRADE, properties);
	    	}    	
    	}
    }
	
	void finishAsync()
	{
		finish();
	}
	
	
    private class AsyncCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            addGrade();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
    		finishAsync();        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }		

}
