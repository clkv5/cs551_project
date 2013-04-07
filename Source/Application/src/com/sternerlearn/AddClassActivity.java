package com.sternerlearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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
    
    public Teacher addClass()
    {
    	Teacher t = SharedData.getInstance().getTeacher();
    	
    	String name = ((TextView)findViewById(R.id.editText)).getText().toString();
    	
    	t.addClass( name );
    	
    	return t;
    }
    
    public void finishAsync()
    {
    	finish();
    }

    private class AsyncCall extends AsyncTask<Void, Void, Teacher> {
        @Override
        protected Teacher doInBackground(Void... params) {
            return addClass();
        }

        @Override
        protected void onPostExecute(Teacher result) {
    		SharedData.getInstance().setTeacher(result);
    		finishAsync();        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }	    
}
