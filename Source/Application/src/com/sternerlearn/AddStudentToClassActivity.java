package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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
	
	void addStudent()
	{
    	Account a = SharedData.getInstance().getAccount();
    	Course c = SharedData.getInstance().mCurrentCourse;
    	
    	String name = ((TextView)findViewById(R.id.editText)).getText().toString();
    	
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aTeacherID", a.mId));
		properties.add(new PropertyWrapper("aPassword", a.mPassword));
		properties.add(new PropertyWrapper("aClassID", c.mId));
		
		// TODO: Get student id from name
		int studentID = 1002;
		properties.add(new PropertyWrapper("aStudentID", studentID));
		
		WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_ADD_STUDENT_TO_CLASS, properties);    	
	}
	
	void finishAsync()
	{
		finish();
	}

    private class AsyncCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            addStudent();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
