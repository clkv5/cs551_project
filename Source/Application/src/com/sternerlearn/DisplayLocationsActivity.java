package com.sternerlearn;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;

public class DisplayLocationsActivity extends FragmentActivity {

	/* Constants */
	private final int MICRODEGREE = 1000000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_display_locations);
		}
		catch( Exception e) {
    		Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
    		toast.show();
			return;
		}
		
		/* Get the locations */
		Student stu = SharedData.getInstance().getStudent();
		ArrayList<Location> locs = stu.mLocations;
		
		/* Create overlay array */
		OverlayItem[] overlays = new OverlayItem[ locs.size() ];
		for( int i = 0; i < locs.size(); i++)
		{
			Location loc = locs.get(i);
			GeoPoint point = new GeoPoint(
											(int)(loc.getLatitude()  * MICRODEGREE),
											(int)(loc.getLongitude() * MICRODEGREE)
										 );
			overlays[i] = new OverlayItem( point, loc.getDate(), "" );
		}
		
		/*  */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_locations, menu);
		return true;
	}

}
