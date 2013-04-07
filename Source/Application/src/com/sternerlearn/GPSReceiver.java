package com.sternerlearn;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class GPSReceiver extends Service
{
	/* GPS constants */
	public static final boolean LOG_TO_DATABASE = true;
	private static final int GPS_MIN_TIME = 300000;    /* In milliseconds */
	private static final int GPS_MIN_DISTANCE = 0;     /* In meters */
	
	/* Debug variables */
	public static String mResponse = "Initial";
	private static final boolean DEBUG = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
	    super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) { 
		/* Create GPS manager */
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		/* Create GPS listener with call-backs for manager */
		LocationListener listener = new LocationListener()
		{
	    	/* Called on a location update */
		    public void onLocationChanged(Location location)
			    {
			    /* Bypass database logging */	
		    	if( !LOG_TO_DATABASE )
		    		return;
		    	
			    /* Start task to log location data  */
			    AsyncCallGPS task = new AsyncCallGPS();
		    	task.execute(location);
		    	
		    	if(DEBUG)
			    	{
		    		CharSequence text = mResponse;
		    		//CharSequence text = WebServiceWrapper.mResponse;
		    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
		    		toast.show();
			    	}
			    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}
		    public void onProviderEnabled(String provider) {}
		    public void onProviderDisabled(String provider) {}
		};
		
		/* Request updates from manager */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, listener);
		
	}
	
    private class AsyncCallGPS extends AsyncTask<Location, Void, Void> {
        @Override
        protected Void doInBackground(Location... location) {
	    	/* Get coordinates */
	    	double lat = location[0].getLatitude();
	    	double lon = location[0].getLongitude();
	    	
	    	/* Get time */
	    	String time = getTimestamp();
	    	
	    	/* Get student ID and password */
	    	Account acct = SharedData.getInstance().getAccount();
	    	int id = acct.mId;
	        String pass = acct.mPassword;
	    	
	    	/* Create list of web method parameters */
	        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
	        paramList.add( new PropertyWrapper( "aStudentID" , id ) );
	        paramList.add( new PropertyWrapper( "aPassword" , pass ) );
	        paramList.add( new PropertyWrapper( "aLatitude" , lat ) );
	        paramList.add( new PropertyWrapper( "aLongitude" , lon ) );
	        paramList.add( new PropertyWrapper( "aTime" , time ) );
	        
	        /* Log to Locations database */
	        SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.PARENT_URL, Types.PARENT_ADD_LOCATION, paramList );
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
	
	@SuppressLint("SimpleDateFormat")
	public static String getTimestamp()
	{
		/* Create time object */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String dt = sdf.format(new Date());
		
    	return dt;
	}
}

