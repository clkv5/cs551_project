package com.sternerlearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.Toast;

public class AssignmentsActivity extends ListActivity {

	private AssignmentAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignments);
		
		// Instead of doing a calendar this will just be a list of upcoming assignments
		AsyncCall task = new AsyncCall();
		task.execute();
		
		mAdapter = new AssignmentAdapter(this, null);
		
		setListAdapter(mAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignments, menu);
		return true;
	}
	
	Student updateAssignments()
	{
		Student s = SharedData.getInstance().getStudent();
		if( s == null )
		{
			s = new Student( SharedData.getInstance().getAccount() );
		}
		
		// Since I'm lazy just do this
		// This will also update their assignments
		s.updateGrades();
		
		return s;
	}
	
	void finishAsync()
	{
		Student s = SharedData.getInstance().getStudent();
		
		mAdapter.setGrades(s.getFutureAssignments());
		mAdapter.notifyDataSetChanged();
		
		CharSequence text = "Finished getting assigns";
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
		toast.show();		
	}
	
    private class AsyncCall extends AsyncTask<Void, Void, Student> {
        @Override
        protected Student doInBackground(Void... params) {
            return updateAssignments();
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
