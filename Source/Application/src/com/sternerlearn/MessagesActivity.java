package com.sternerlearn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class MessagesActivity extends Activity {

	/* Page widgets */
	private Button   submitButton;
	private TextView textView;
	private DatePicker datePicker;
	
	/* Debug variables */
	public String mResponse = "Initial";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);

		/* Create button click event */
		submitButton = (Button)findViewById( R.id.messageGoButton );
		textView = (TextView)findViewById( R.id.messagesEditText );
		
		/* Set datePicker params */
		datePicker = (DatePicker)findViewById( R.id.messageDatePicker );
		datePicker.setCalendarViewShown(false);
		Calendar calendar = Calendar.getInstance();
		
		datePicker.setMaxDate(calendar.getTimeInMillis());
		
		createButtonListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
		return true;
	}
	
	
	private void createButtonListener()
	{
	    /* Create submit button event */
	    submitButton.setOnClickListener( new View.OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		viewMessagesFromDate();
	    	}  // onClick()
	    });  //setOnClickListener()
	}
	
	@SuppressLint("SimpleDateFormat")
	public void viewMessagesFromDate()
	{
		int day, month, year;
		String dateString;
		
		/* Get input info */
		try {
			datePicker = (DatePicker)findViewById( R.id.messageDatePicker );
			day = datePicker.getDayOfMonth();
			month = datePicker.getMonth() + 1;
			year = datePicker.getYear();
		}
		catch(Exception ex) {
    		CharSequence text = "Error parsing date.";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
			return;
		}
		
		/* Convert input to timestamp */
		dateString = convertToTimestamp(year, month, day, 0, 0, 0);
		
		/* Query database */
		AsyncCallGetMessages task = new AsyncCallGetMessages();
		task.execute(dateString);

	}
	
    private class AsyncCallGetMessages extends AsyncTask< String, Void, Boolean > {
        @Override
        protected Boolean doInBackground(String... date) {

        	/* Get web service params */
    		Account acct = SharedData.getInstance().getAccount();
    		int studentID = acct.mLinkedId;
    		String endTime = date[0].substring(0, 11);   //Include T
    		endTime += "23:59:59.000000000";             //Go to end of day
    		
	        /* Get student account */
	        Student stuAcc = SharedData.getInstance().getStudent();
	        if( stuAcc == null)
	        {
	        	stuAcc = new Student( SharedData.getInstance().getAccount() );
	        	SharedData.getInstance().setStudent(stuAcc);
	        }
	        stuAcc.mMessages = new ArrayList<Message>();
    		
	    	/* Create list of web method parameters */
	        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
	        paramList.add( new PropertyWrapper( "aStudentID" , studentID ) );
	        paramList.add( new PropertyWrapper( "aStartTime" , date[0] ) );     
	        paramList.add( new PropertyWrapper( "aEndTime" , endTime ) );

	        try
	        {
	        	/* Call web service */
	        	SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.PARENT_URL, Types.PARENT_GET_MESSAGES, paramList );
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
					   			String aSender = arr.getProperty("aSender").toString();
					   			String aMessage = arr.getProperty("aMessage").toString();
					   			String aTime = arr.getProperty("mTime").toString();
					   			
					   			stuAcc.mMessages.add( new Message( aSender, aMessage, aTime) );
					   		}
			    		}
			    		mResponse = "Property count:  " + Integer.toString( response.getPropertyCount() );
		    			return true;
	    			}
	    			/* no messages received */
	    			else
	    			{
	    				mResponse = "No messages exist for this date";
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
        		/* launch DisplayMessagesActivity */
        		Intent page = new Intent(getApplicationContext(), DisplayMessagesActivity.class);
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

	public static String convertToTimestamp( int year, int month, int day, int hour, int minute, int second )
	{
		/*  Note:  does not check for improper bounds */
		
		String ret;
		try {
			String aDay = (day < 10) ? "0" + Integer.toString(day) : Integer.toString(day);
			String aMonth =  (month < 10) ? "0" + Integer.toString(month) : Integer.toString(month);
			String aYear = Integer.toString(year);
			String aHour = (hour < 10) ? "0" + Integer.toString(hour) : Integer.toString(hour);
			String aMinute = (minute < 10) ? "0" + Integer.toString(minute) : Integer.toString(minute);
			String aSecond = (second < 10) ? "0" + Integer.toString(second) : Integer.toString(second);
			
			ret = aYear + "-" + aMonth + "-" + aDay + "T" + aHour + ":" + aMinute + ":" + aSecond + ".000000000";
		}
		catch(Exception ex) {
			ret = ex.toString();
		}
		
    	return ret;
	}
    
}
