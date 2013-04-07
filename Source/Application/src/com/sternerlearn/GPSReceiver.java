package com.sternerlearn;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import java.util.List;
import java.util.ArrayList;

public class GPSReceiver extends Service
{

	private static final int GPS_MIN_TIME = 5000;     /* In milliseconds */
	private static final int GPS_MIN_DISTANCE = 50;   /* In meters */
	
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
		    	/* Get coordinates */
		    	double lat = location.getLatitude();
		    	double lon = location.getLongitude();
		    	
		    	/* Get time */
		    	String time = getDateTime();
		    	
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
		        obj = Double.valueOf(lat);
		        paramList.add( new PropertyWrapper( "aLatitude" , obj ) );
		        obj = Double.valueOf(lon);
		        paramList.add( new PropertyWrapper( "aLongitude" , obj ) );
		        obj = String.valueOf(time);
		        paramList.add( new PropertyWrapper( "aTime" , obj ) );
		        
		        /* Log to Locations database */
		        WebServiceWrapper webConn = WebServiceWrapper.getInstance();
		    	webConn.call(Types.STUDENT_URL, Types.PARENT_ADD_LOCATION, paramList );
		    }

		    /* Called when GPS is enabled or disabled */
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    /* Called when GPS is enabled */
		    public void onProviderEnabled(String provider) {}

	        /* Called when GPS is disabled */
		    public void onProviderDisabled(String provider) {}
		};
		
		/* Request updates from manager */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, listener);
		
	}
	
	
	public static String getDateTime()
	{
		/* Create time object */
    	Time current = new Time();
    	current.setToNow();
    	
    	/* Convert fields to string */
    	String stamp = String.valueOf(current.year) + "-" + String.valueOf(current.month) + "/" +
    			       String.valueOf(current.monthDay);
    	stamp += " " + String.valueOf(current.hour) + ":" + String.valueOf(current.minute) + ":" +
    			       String.valueOf(current.second);
    	
    	return stamp;
	}
	
}
