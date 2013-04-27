package com.sternerlearn;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

public class DisplayLocationsActivity extends android.support.v4.app.FragmentActivity {

	/* Constants */
	private final int MICRODEGREE = 1000000;
	private final int INITIAL_ZOOM = 15;
	
	/* Variables */
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/* Open the map display */
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_display_locations);
			map = ( (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map) ).getMap();
		}
		catch( Exception e) {
			//String msg = e.toString();
			String msg = "Error loading map";
    		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    		toast.show();
			return;
		}
		
		/* Get locations */
		Student stu = SharedData.getInstance().getStudent();
		if(stu == null)
		{
			CharSequence text = "Student is null";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
    		return;
		}
		
		ArrayList<Location> locs = stu.mLocations;
			if( locs == null)
		{
			locs = new ArrayList<Location>();
			CharSequence text = "Location is null";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
    		return;
		}
		locs.add( new Location( 39.1f, 94.5783f, "DATE") );
		
		/* Move map to current location */
		if( locs.size() > 0 )
		{
			LatLng loc = new LatLng( locs.get(0).getLatitude(), locs.get(0).getLongitude() );
			map.moveCamera( CameraUpdateFactory.newLatLngZoom(loc, INITIAL_ZOOM) );
			
			CharSequence text = "Moving location";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
		}
		
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
		
		/* Display overlays on map */
		for( int i = 0; i < overlays.length ; i++ )
		{
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_locations, menu);
		return true;
	}

}
