package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddAssignmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		
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
		getMenuInflater().inflate(R.menu.add_assignment, menu);
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
	
	String addAssign()
	{
    	String name = ((EditText)findViewById( R.id.className )).getText().toString();  
    	String desc = ((EditText)findViewById( R.id.desc )).getText().toString(); 
    	
    	NumberPicker picker = (NumberPicker)findViewById(R.id.numberPicker1);
    	double val = (double)picker.getValue();
    	
		// Selected date
		DatePicker datePicker = (DatePicker)findViewById( R.id.datePicker1 );
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1;
		int year = datePicker.getYear();
		
		// Convert input to timestamp
		String timeString = MessagesActivity.convertToTimestamp(year, month, day, 0, 0, 0);    	
    	
    	Account a = SharedData.getInstance().getAccount();
    	
    	Course c = SharedData.getInstance().mCurrentCourse;
    	
    	/* Create list of web method parameters */
        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
        paramList.add( new PropertyWrapper( "aTeacherID" , a.mId ) );
        paramList.add( new PropertyWrapper( "aPassword" , a.mPassword ) );
        paramList.add( new PropertyWrapper( "aClassID" , c.mId ) );
        paramList.add( new PropertyWrapper( "aTotalPoints" , val ) );
        paramList.add( new PropertyWrapper( "aTest" , "false" ) );
        paramList.add( new PropertyWrapper( "aDateDue" , timeString ) );
        paramList.add( new PropertyWrapper( "aName" , name ) );
        paramList.add( new PropertyWrapper( "aDescription" , desc ) );
        
        /* Log to database */
        SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_ADD_ASSIGNMENT, paramList );
        
        String ret = "Successfully added assignment!";
        
        try
        {
        	SoapPrimitive primitive = (SoapPrimitive)envelope.getResponse();
        	
        	if( primitive.toString().compareTo("false") == 0 )
        	{
        		ret = "Error creating assignment.";
        	}
        }
        catch( Exception ex) {}
        
        return ret;
	}
	
	void finishAsync( String res )
	{
		Toast toast = Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG);
		toast.show();	
		
		finish();
	}

    private class AsyncCall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return addAssign();
        }

        @Override
        protected void onPostExecute(String result) {
    		finishAsync( result );        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }		
}
