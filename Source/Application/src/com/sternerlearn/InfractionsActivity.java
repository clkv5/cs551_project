package com.sternerlearn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class InfractionsActivity extends ListActivity {
	
	private InfractionsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infractions);
		
		// Instead of doing a calendar this will just be a list of upcoming assignments
		AsyncCall task = new AsyncCall();
		task.execute();
		
		mAdapter = new InfractionsAdapter(this, null);
		
		setListAdapter(mAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.infractions, menu);
		return true;
	}
	
	ArrayList<Infraction> updateInfractions()
	{
		ArrayList<Infraction> infractions = new ArrayList<Infraction>();
		
		Account acct = SharedData.getInstance().getAccount();
		
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aStudentID", acct.mId));
		
		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_INFRACTIONS, properties);
		
		// TODO: Fix the layout, make it horizontal layouts?
		
	   	/* Call web service */
    	try 
    	{	
    		SoapObject response = (SoapObject)envelope.getResponse();

    		// It's a list of infractions
    		for( int i = 0; i < response.getPropertyCount(); i++ )
    		{
    			SoapObject arr = (SoapObject)response.getProperty(i);
    		
		    		
		   		if( arr.hasProperty("mInfractionType") &&
		   			arr.hasProperty("mDescription") &&
		   			arr.hasProperty("mDate") )
		   		{
		   			
		   			Integer type = java.lang.Integer.parseInt( arr.getProperty("mInfractionType").toString() );
		   			String desc = arr.getProperty("mDescription").toString();
		   			Timestamp date = Assignment.hackDate( arr.getProperty("mDate").toString() );
		   			
		   			infractions.add( new Infraction( type, desc, date ) );
		   			
		   		}
    		}
    	}
    	catch(Exception ex) {}
    			
		
		return infractions;
	}
	
	void finishAsync( ArrayList<Infraction> infractions )
	{
		mAdapter.setData(infractions);
		mAdapter.notifyDataSetChanged();
		
	}
	
    private class AsyncCall extends AsyncTask<Void, Void, ArrayList<Infraction>> {
        @Override
        protected ArrayList<Infraction> doInBackground(Void... params) {
            return updateInfractions();
        }

        @Override
        protected void onPostExecute(ArrayList<Infraction> result) {
    		finishAsync( result );        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }	

}
