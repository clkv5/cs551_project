package iteration2.sms;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//SMS imports
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
import android.telephony.SmsMessage;

/*
 * http://www.apriorit.com/our-company/dev-blog/227-handle-sms-on-android
 */
public class SmsReceiver extends BroadcastReceiver {

	public static final String SMS_EXTRA_NAME ="pdus";
	
	public void onReceive( Context context, Intent intent ) 
	{
	    // Get the SMS map from Intent
	    Bundle extras = intent.getExtras();
	     
	    String messages = "";
	     
	    if ( extras != null )
	    {
	        // Get received SMS array
	        Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
	         
	        // Parse through SMS bytes
	        for ( int i = 0; i < smsExtra.length; ++i )
	        {
	            SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
	             
	            String body = sms.getMessageBody().toString();
	            String address = sms.getOriginatingAddress();
	             
	            messages += "SMS from " + address + " :\n";                    
	            messages += body + "\n";
	             
	        }
	         
	        // Display SMS message
	        Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
	    }
	     
	}

}
