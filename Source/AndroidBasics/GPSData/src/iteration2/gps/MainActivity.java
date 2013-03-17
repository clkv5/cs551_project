package iteration2.gps;

// General imports
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Context;

// GPS imports
// Must also add uses-permission to manifest
import android.location.*;

// Toast imports
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/* Create GPS manager */
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		/* Create GPS listener with call-backs for manager */
		LocationListener listener = new LocationListener()
		{
	    	// called when the listener is notified with a location update from the GPS
		    public void onLocationChanged(Location location)
		    {
		    	double lat = location.getLatitude();
		    	double lon = location.getLongitude();
		    	
		    	String latstr = Double.toString(lat);
		    	String lonstr = Double.toString(lon);
		    	String loc = latstr + " " + lonstr;
		    	
		    	showMsg(loc);
		    }

		    // called when the status of the GPS provider changes
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    // called when the GPS provider is turned on (user turning on the GPS on the phone)
		    public void onProviderEnabled(String provider) {}

	        // called when the GPS provider is turned off (user turning off the GPS on the phone)
		    public void onProviderDisabled(String provider) {}
		};
		
		/* Request updates from manager */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, listener);
	}
	
	protected void showMsg(String msg) {
		Toast toastMessage;
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;
		
		toastMessage = Toast.makeText(context, text, duration);
		toastMessage.show();
		
	}

}
