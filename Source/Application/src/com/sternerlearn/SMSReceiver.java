package com.sternerlearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class SMSReceiver extends BroadcastReceiver {
	
	/* Debug variables */
	public static boolean LOG_TO_DATABASE = false;
	private static final String SMS_PRTCL ="pdus";
	
	/* SMS variables */
    public static String mResponse = "Initial";
	
	public void onReceive( Context context, Intent intent ) 
	{
		/* Return if not student account */
		Account acct = SharedData.getInstance().getAccount();
		if( acct == null || 
			( Types.AccountType.STUDENT.ordinal() != acct.mAccountType ) ||
	        (LOG_TO_DATABASE == false) )
		{
			mResponse = "Wrong account type";
			return;
		}
		
	    /* Get the SMS map from Intent */
	    Bundle extras = intent.getExtras();
	    if( extras == null )
	    {
	    	mResponse = "Null broadcast data";
	    	return;
	    }
	    
	    /* Start task to log message data  */
	    AsyncCallSMS task = new AsyncCallSMS();
    	task.execute(extras);
    	
    	/* Debug */
    	while( mResponse == "Initial");
		CharSequence text = mResponse;
		//CharSequence text = WebServiceWrapper.mResponse;
		Toast toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG);
		toast.show();
	}
	
	
    private class AsyncCallSMS extends AsyncTask<Bundle, Void, Void> {
        @Override
        protected Void doInBackground(Bundle... extras) {

	        /* Retrieve SMS bytes */
	        Object[] smsExtra = (Object[]) extras[0].get( SMS_PRTCL );
	         
	        /* Convert SMS message parts to string */
	        String sender = "";
	        String message = "";
	        for ( int i = 0; i < smsExtra.length; ++i )
	        {
	            SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
	           
	            sender = sms.getOriginatingAddress();
	            message += sms.getMessageBody().toString();     
	        }
	        
	    	/* Get time */
	    	String time = GPSReceiver.getTimestamp();
	        
	    	/* Get student ID and password */
			Account acct = SharedData.getInstance().getAccount();
	    	int id = acct.mId;
	        String pass = acct.mPassword;
    	    
	    	/* Create list of web method parameters */
	        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
	        paramList.add( new PropertyWrapper( "aStudentID" , id ) );
	        paramList.add( new PropertyWrapper( "aPassword" , pass ) );
	        paramList.add( new PropertyWrapper( "aSender" , sender ) );
	        paramList.add( new PropertyWrapper( "aMessage" , message ) );
	        paramList.add( new PropertyWrapper( "aTime" , time ) );
    	        	    
	        /* Log to Messages database */
	        SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.PARENT_URL, Types.PARENT_ADD_MESSAGE, paramList  );
	        try {
				mResponse = envelope.getResponse().toString();
			} catch (SoapFault e) {
				mResponse = "SOAP fault on return";
			}
	        return null;
        }

        @Override
        protected void onPostExecute(Void value) {}

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
}
