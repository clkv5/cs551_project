package com.sternerlearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
	
	/* Debug variables */
	public String mResponse = "Initial";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);

		/* Create button click event */
		submitButton = (Button)findViewById( R.id.messageGoButton );
		textView = (TextView)findViewById( R.id.messagesEditText );
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
		int year, month, day;
		String aYear, aMonth, aDay;
		
		/* Get input info */
		try {
			aYear = ((EditText)findViewById( R.id.messageYYYY )).getText().toString();
			year = Integer.parseInt( aYear );
			aMonth = ((EditText)findViewById( R.id.messageMM )).getText().toString();
			month = Integer.parseInt( aMonth );
			aDay = ((EditText)findViewById( R.id.messageDD )).getText().toString();
			day = Integer.parseInt( aDay );
		}
		catch(Exception ex) {
			textView.setText("Unable to retrieve info.");
			return;
		}
		
		/* Get input info and current date */

		Date request;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			request = sdf.parse( aYear + "-" + aMonth + "-" + aDay );
		}
		catch(ParseException ex) {
			textView.setText("Please enter a valid date.");
			return;
		}
		
		/* Get current date and query database if request is valid */
		Date current = new Date();
		if( request.compareTo(current) > 0 )
		{
			textView.setText("Please enter a valid date.");
		}
		else
		{
			String date = convertToTimestamp( day, month, year );
			
			AsyncCallGetMessages task = new AsyncCallGetMessages();
			task.execute(date);
			
			
			while(mResponse == "Initial");
    		Toast toast = Toast.makeText(getApplicationContext(), mResponse, Toast.LENGTH_LONG);
    		toast.show();
		}
	}
	
    private class AsyncCallGetMessages extends AsyncTask<String, Void, Boolean > {
        @Override
        protected Boolean doInBackground(String... time) {
        	
        	boolean success;
        	
        	/* Get student's ID */
    		Account acct = SharedData.getInstance().getAccount();
    		int studentID = acct.mLinkedId;

        	/* Get current time */
    		String currTime = GPSReceiver.getTimestamp();

	    	/* Create list of web method parameters */
	        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
	        paramList.add( new PropertyWrapper( "aStudentID" , studentID ) );
	        paramList.add( new PropertyWrapper( "aStartTime" , time[0] ) );
	        paramList.add( new PropertyWrapper( "aEndTime" , currTime ) );
	        //TODO:  convert currTime to a timestamp

	        try {
	        	/* Call web service */
	        	SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.PARENT_URL, Types.PARENT_GET_LOCATIONS, paramList );
	    		SoapObject response = (SoapObject)envelope.getResponse();
	    		mResponse = response.toString() + " " + response.getPropertyCount();
				
	    		/* Parse XML return */
	    		List<Message> mMessages = new ArrayList<Message>();
	    		for( int i = 0; i < response.getPropertyCount(); i++ )
	    		{
		    		SoapObject arr = (SoapObject)response.getProperty(i);
		    		
		   			String aSender = arr.getProperty("aSender").toString();
		   			String aMessage = arr.getProperty("aMessage").toString();
		   			String aTime = arr.getProperty("mTime").toString();
		   			
		   			mMessages.add( new Message( aSender, aMessage, aTime) );
	    		}
	    		
	    		/* Put list in Student account */
	    		Student stuAcc = SharedData.getInstance().getStudent();
	    		stuAcc.mMessages = mMessages;
	    		
	    		success = true;
	    		
			} catch (Exception ex) {
				mResponse = ex.toString();
				success = false;
			}
	        
	        return success;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
        	if(success)
        	{
        	
        	}
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }

	@SuppressLint("SimpleDateFormat")
	public static String convertToTimestamp( int year, int month, int day )
	{
		/* Create time object */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		calendar.set(year, month, day, 0, 0, 0 );
		sdf.setCalendar(calendar);
		String dt = sdf.format( calendar.getTime() ); 
		
    	return dt;
	}
    
}
