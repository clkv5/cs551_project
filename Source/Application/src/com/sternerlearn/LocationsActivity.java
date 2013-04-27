package com.sternerlearn;

import java.util.Calendar;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class LocationsActivity extends Activity {

	/* Page widgets */
	private Button   submitButton;
	private DatePicker datePicker;
	private TimePicker timePicker;
	
	/* Debug variables */
	public String mResponse = "Initial";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		
		/* Create button click event */
		submitButton = (Button)findViewById( R.id.locationsGoButton );
		
		/* Set datePicker params */
		datePicker = (DatePicker)findViewById( R.id.locationsDatePicker );
		datePicker.setCalendarViewShown(false);
		Calendar calendar = Calendar.getInstance();
		datePicker.setMaxDate(calendar.getTimeInMillis());
		
		/* Set timePicker */
		timePicker = (TimePicker)findViewById( R.id.locationsTimePicker );
		
		/* Create button listener */
		createButtonListener();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locations, menu);
		return true;
	}

	
	private void createButtonListener()
	{
	    /* Create submit button event */
	    submitButton.setOnClickListener( new View.OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		viewLocationsFromTime();
	    	}  // onClick()
	    });  //setOnClickListener()
	}
	
	private void viewLocationsFromTime()
	{
		int year, month, day, hour, minute;
		String timeStringBegin, timeStringEnd;
		
		/* Get input info */
		try {
			//date
			datePicker = (DatePicker)findViewById( R.id.locationsDatePicker );
			day = datePicker.getDayOfMonth();
			month = datePicker.getMonth() + 1;
			year = datePicker.getYear();
			
			//start time
			timePicker = (TimePicker)findViewById( R.id.locationsTimePicker );
			hour = timePicker.getCurrentHour();
			minute = timePicker.getCurrentMinute();
			
		}
		catch(Exception ex) {
    		CharSequence text = "Error parsing widget info.";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
			return;
		}
		
		/* Convert input to timestamps */
		//TODO:  consider adding a variable time span
		timeStringBegin = MessagesActivity.convertToTimestamp(year, month, day, hour, minute, 0);
		if( hour < 23 )
			timeStringEnd = MessagesActivity.convertToTimestamp(year, month, day, hour + 1, minute, 0);
		else
			timeStringEnd = MessagesActivity.convertToTimestamp(year, month, day + 1, 0, minute, 0);
		
		/* Call asynch task */
		AsyncCallGetLocations task = new AsyncCallGetLocations();
		task.execute(timeStringBegin, timeStringEnd);
	}
	
    private class AsyncCallGetLocations extends AsyncTask< String, Void, Boolean > {
    	
        @Override
        protected Boolean doInBackground(String... time)
        {
	        /* Get accounts */
        	Account acct = SharedData.getInstance().getAccount();
        	int studentID = acct.mLinkedId;
        	
	        Student stuAcc = SharedData.getInstance().getStudent();
	        if( stuAcc == null)
	        {
	        	stuAcc = new Student( SharedData.getInstance().getAccount() );
	        	SharedData.getInstance().setStudent(stuAcc);
	        }
	        stuAcc.mLocations = new ArrayList<Location>();
        	
	    	/* Create list of web method parameters */
	        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
	        paramList.add( new PropertyWrapper( "aStudentID" , studentID ) );
	        paramList.add( new PropertyWrapper( "aStartTime" , time[0] ) );     
	        paramList.add( new PropertyWrapper( "aEndTime" , time[1] ) );
        	
	        try
	        {
	        	/* Call web service */
	        	SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.PARENT_URL, Types.PARENT_GET_LOCATIONS, paramList );
	    		SoapObject response = (SoapObject)envelope.getResponse();
	    		if( response == null )
	    		{
	    			mResponse = "No web response received"; 
	    			return false;
	    		}
	    		else
	    		{
	    			/* Parse XML return */
	    			if( response.getPropertyCount() > 0 )
	    			{
			    		for( int i = 0; i < response.getPropertyCount(); i++ )
			    		{
				    		SoapObject arr = (SoapObject)response.getProperty(i);
				    		if( arr == null )
				    		{
				    			mResponse = "Property null:  " + Integer.toString(i);
				    			return false;
				    		}
				    		
					   		if( !arr.hasProperty("aSender") ||
						   		!arr.hasProperty("aMessage") ||
						   		!arr.hasProperty("mTime") )
						   	{
					   			mResponse = "Property info null:  " + Integer.toString(i);
						   	}
					   		else
					   		{
					   			Float aLat = Float.parseFloat(arr.getProperty("mLatitude").toString() );
					   			Float aLon = Float.parseFloat(arr.getProperty("mLongitude").toString() );
					   			String aTime = arr.getProperty("mTime").toString();
					   			
					   			stuAcc.mLocations.add( new Location( aLat, aLon, aTime) );
					   		}
			    		}
			    		mResponse = "Property count:  " + Integer.toString( response.getPropertyCount() );
		    			return true;
	    			}
	    			/* no messages received */
	    			else
	    			{
	    				mResponse = "No locations exist for the given time";
	    				return false;
	    			}
	    		}
	        }
	        catch (Exception ex)
	        {
				mResponse = ex.toString();
				return false;
			}
        }
        
        @Override
        protected void onPostExecute(Boolean success)
        {
        	if(success)
        	{
        		/* launch DisplayLocationsActivity */
        		Intent page = new Intent(getApplicationContext(), DisplayLocationsActivity.class);
        		startActivity(page);
        	}
        	else
        	{
        		Toast toast = Toast.makeText(getApplicationContext(), mResponse, Toast.LENGTH_LONG);
        		toast.show();
        	}
        }
        
        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
}
