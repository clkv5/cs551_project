package com.sternerlearn;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.format.Time;

public class GPSActivity extends Activity {

	/* Web service constants */
	private static String METHOD_NAME = "AddLocation";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
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
		    	//TODO
		    	int id = 0;
		        String pass = "";
		    	
		    	/* Log to Locations database */
		    	//TODO
		    	
		    }

		    // called when the status of the GPS provider changes
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    // called when the GPS provider is turned on (user turning on the GPS on the phone)
		    public void onProviderEnabled(String provider) {}

	        // called when the GPS provider is turned off (user turning off the GPS on the phone)
		    public void onProviderDisabled(String provider) {}
		};
		
		/* Request updates from manager */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, listener);
		
	}
	
	public String getDateTime()
	{
		/* Create time object */
    	Time current = new Time();
    	current.setToNow();
    	
    	/* Convert fields to string */
    	String stamp = String.valueOf(current.year) + "-" + String.valueOf(current.month) + "-" +
    			       String.valueOf(current.monthDay);
    	stamp += " " + String.valueOf(current.hour) + ":" + String.valueOf(current.minute) + ":" +
    			       String.valueOf(current.second);
    	
    	return stamp;
	}

}
