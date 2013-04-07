package com.sternerlearn;

import java.util.ArrayList;
import java.util.Set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class GradesActivity extends ListActivity
{
	private ArrayList<String> mListItems = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grades);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, mListItems );
		setListAdapter( mAdapter );
		
		AsyncCall task = new AsyncCall();
		task.execute();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grades, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public Student getStudent()
	{
		Student s = SharedData.getInstance().getStudent();
		if( s == null )
		{
			s = new Student( SharedData.getInstance().getAccount() );
		}
		return s;
	}
	
	public void finishAsync()
	{
		Student s = SharedData.getInstance().getStudent();
		
		Set<Course> courses = s.mCourses.keySet();
		
		for( Course c : courses )
		{
			mListItems.add(c.mClassName);
		}
		
		mAdapter.notifyDataSetChanged();
		
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		// Sigh, maybe I should have designed this a bit more intelligently
		Student s = SharedData.getInstance().getStudent();
		Set<Course> courses = s.mCourses.keySet();
		
		// Find the class with this name
		for( Course c : courses )
		{
			if( mListItems.get(position) == c.mClassName )
			{
				SharedData.getInstance().mCurrentCourse = c;
				break;
			}
		}
		
		Intent pg = new Intent(this, ClassesActivity.class);
		startActivity( pg );
	}
	
	
    private class AsyncCall extends AsyncTask<Void, Void, Student> {
        @Override
        protected Student doInBackground(Void... params) {
            return getStudent();
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
