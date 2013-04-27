package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudentToClassActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_student_to_class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_student_to_class, menu);
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
	
	String addStudent()
	{
    	Account a = SharedData.getInstance().getAccount();
    	Course c = SharedData.getInstance().mCurrentCourse;
    	
    	String name = ((TextView)findViewById(R.id.editText)).getText().toString();
    	
    	// Try to get the student ID
    	int studentID = -1;
		try
		{
			List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
			properties.add(new PropertyWrapper("aTeacherID", a.mId));
			properties.add(new PropertyWrapper("aPassword", a.mPassword));
			properties.add(new PropertyWrapper("aStudentName", name));
			
			SoapSerializationEnvelope env = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_STUDENT, properties);
			SoapPrimitive p = (SoapPrimitive)env.getResponse();
			
			studentID = java.lang.Integer.parseInt( p.toString() );
			
		}
		catch( Exception ex ) {}
		
		String ret = "Error adding student to class";
    	
    	if( studentID > 0 )
    	{
			List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
			properties.add(new PropertyWrapper("aTeacherID", a.mId));
			properties.add(new PropertyWrapper("aPassword", a.mPassword));
			properties.add(new PropertyWrapper("aClassID", c.mId));
			properties.add(new PropertyWrapper("aStudentID", studentID));
			
			SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_ADD_STUDENT_TO_CLASS, properties);
			
	        try
	        {
	        	SoapPrimitive primitive = (SoapPrimitive)envelope.getResponse();
	        	
	        	if( primitive.toString().compareTo("true") == 0 )
	        	{
	        		ret = "Successfully added student to class!";
	        	}
	        }
	        catch( Exception ex) {}	   
    	}
    	
    	return ret;
	}
	
	void finishAsync( String res )
	{
		Toast toast = Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG);
		toast.show();	
		
		finish();
	}

    private class AsyncCall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return addStudent();
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
