package com.sternerlearn;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

public class GPSReceiver extends Service
{
	private static final int GPS_MIN_TIME = 6000;     /* In milliseconds */
	private static final int GPS_MIN_DISTANCE = 0;     /* In meters */
	
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
		    AsyncCallGPS task = new AsyncCallGPS();
	    	task.execute(location);
	    	
	    	if(DEBUG)
		    	{
	    		//CharSequence text = mResponse;
	    		CharSequence text = WebServiceWrapper.mResponse;
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
	    	String time = getDateTime();
	    	
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
	        
	        /* Debug */
	        mResponse = Integer.toString(id) + " " + pass + " " + Double.toString(lat) + " " +
	                    Double.toString(lon) + " " + time;
	        
	        /* Log to Locations database */
	        WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.PARENT_ADD_LOCATION, paramList );
        	return null;
        }

        @Override
        protected void onPostExecute(Void value) {
       	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }     
	
	public static String getDateTime()
	{
		/* Create time object */
    	Time current = new Time();
    	current.setToNow();
    	
    	/* Convert fields to string */
    	String stamp = String.valueOf(current.month) + "/" + String.valueOf(current.monthDay) + "/" +
    			       String.valueOf(current.year);
    	stamp += " " + String.valueOf(current.hour) + ":" + String.valueOf(current.minute) + ":" +
    			       String.valueOf(current.second);
    	
    	return stamp;
	}
	
}

