package com.sternerlearn;

import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddClassActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_class, menu);
		return true;
	}
	
    public void handleSubmit
		(
		View aView
		)
	{
		AsyncCall task = new AsyncCall();
		task.execute();
	} 
    
    public String addClass()
    {
    	Teacher t = SharedData.getInstance().getTeacher();
    	
    	String name = ((TextView)findViewById(R.id.editText)).getText().toString();
    	
    	SoapSerializationEnvelope envelope = t.addClass( name );
    	
        String ret = "Successfully added class!";
        
        try
        {
        	SoapPrimitive primitive = (SoapPrimitive)envelope.getResponse();
        	
        	if( primitive.toString().compareTo("false") == 0 )
        	{
        		ret = "Error creating class";
        	}
        }
        catch( Exception ex) {}
        
        SharedData.getInstance().setTeacher(t);
        
        return ret;    	
    }
    
    public void finishAsync(String res)
    {
		Toast toast = Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG);
		toast.show();	    	
    	
    	finish();
    }

    private class AsyncCall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return addClass();
        }

        @Override
        protected void onPostExecute(String result) {
    		
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
