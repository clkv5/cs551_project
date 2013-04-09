package com.sternerlearn;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeacherMainMenu extends ListActivity {
	
	private ArrayList<String> mListItems = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	
	AsyncCall mTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_main_menu);
		
		mAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, mListItems );
		setListAdapter( mAdapter );
		
		mTask = new AsyncCall();
		mTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_main_menu, menu);
		return true;
	}
	
	protected void onListItemClick( ListView aView, View v, int position, long id )
	{
		if( 0 == position )
		{
			// This is the "Add Class" option
			// Push a page to create a new class in the DB
			startActivity(new Intent(this, AddClassActivity.class));
		}
		else
		{
			// Decrement position to account for the fake thing and set the current course
			position--;
			SharedData.getInstance().mCurrentCourse = SharedData.getInstance().getTeacher().mCourses.get(position);
			
			startActivity(new Intent(this, ModifyClassActivity.class));
		}
	}	
	
	@Override
	public void onWindowFocusChanged( boolean hasFocus )
	{
		super.onWindowFocusChanged(hasFocus);
		
		// Re-run the task if we get focus and aren't already running
		if( hasFocus )
		{
			if( mTask == null || AsyncTask.Status.RUNNING != mTask.getStatus() )
			{
				mTask = new AsyncCall();
				mTask.execute();
			}
		}
	}
	
	Teacher getClasses()
	{
		Teacher t = SharedData.getInstance().getTeacher();
		if( t == null )
		{
			t = new Teacher( SharedData.getInstance().getAccount() );
		}
		
		t.updateClasses();
		
		return t;
	}
	
	void finishAsync()
	{
		mListItems.clear();
		mListItems.add("Add Class");
		
		Teacher t = SharedData.getInstance().getTeacher();
		for( Course c : t.mCourses )
		{
			mListItems.add(c.mClassName);
		}
		
		mAdapter.notifyDataSetChanged();
	}
	
    private class AsyncCall extends AsyncTask<Void, Void, Teacher> {
        @Override
        protected Teacher doInBackground(Void... params) {
            return getClasses();
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
