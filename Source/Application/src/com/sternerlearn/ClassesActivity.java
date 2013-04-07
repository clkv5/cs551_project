package com.sternerlearn;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ClassesActivity extends ListActivity {

	private TwoLineAdapter mAdapter;
	private ArrayList<Grade> mGrades = new ArrayList<Grade>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classes);
		
		AsyncCall task = new AsyncCall();
		task.execute();
		
		mAdapter = new TwoLineAdapter(this, mGrades);
		
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.classes, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		// Not handling clicks for now
	}	
	
	public Student updateGrades()
	{
		Student student = SharedData.getInstance().getStudent();
		
		student.updateGrades();
		
		return student;
	}
	
	public void finishAsync()
	{
		Student s = SharedData.getInstance().getStudent();
		Course c = SharedData.getInstance().mCurrentCourse;
		
		mGrades = s.mCourses.get(c);
		
		mAdapter.setGrades(mGrades);
		mAdapter.notifyDataSetChanged();
		
		CharSequence text = "Finished getting grades";
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
		toast.show();
	}
	
    private class AsyncCall extends AsyncTask<Void, Void, Student> {
        @Override
        protected Student doInBackground(Void... params) {
            return updateGrades();
        }

        @Override
        protected void onPostExecute(Student result) {
    		SharedData.getInstance().setStudent(result);
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
