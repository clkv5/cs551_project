package com.sternerlearn;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	
	/* SMS constants */
	private static final String SMS_PRTCL ="pdus";

	/* Web service constants */
    private static String METHOD_NAME = "AddMessage";
	
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
	         
	        /* Log to Texts database */
	        
	                
	    }
	}
	
	
}
