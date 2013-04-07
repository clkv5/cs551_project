package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	
	private static final String SMS_PRTCL ="pdus";
	
	public void onReceive( Context context, Intent intent ) 
	{
	    /* Get the SMS map from Intent */
	    Bundle extras = intent.getExtras();
	     
	    if ( extras != null )
	    {
	        /* Retrieve SMS bytes */
	        Object[] smsExtra = (Object[]) extras.get( SMS_PRTCL );
	         
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
	    	String time = GPSReceiver.getDateTime();
	        
	    	/* Get student ID and password */
	    	Account acct = SharedData.getInstance().getAccount();
	    	int id = acct.mId;
	        String pass = acct.mPassword;
	         
	    	/* Create list of web method parameters */
	        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
	        Object obj = Integer.valueOf(id);
	        paramList.add( new PropertyWrapper( "aStudentID" , obj ) );
	        obj = String.valueOf(pass);
	        paramList.add( new PropertyWrapper( "aPassword" , obj ) );
	        obj = String.valueOf(sender);
	        paramList.add( new PropertyWrapper( "aSender" , obj ) );
	        obj = Double.valueOf(message);
	        paramList.add( new PropertyWrapper( "aMessage" , obj ) );
	        obj = String.valueOf(time);
	        paramList.add( new PropertyWrapper( "aTime" , obj ) );
	        
	        /* Log to Texts database */
	        WebServiceWrapper webConn = WebServiceWrapper.getInstance();
	    	webConn.call(Types.STUDENT_URL, Types.PARENT_ADD_MESSAGE, paramList );
	                
	    }
	}
	
	
}
