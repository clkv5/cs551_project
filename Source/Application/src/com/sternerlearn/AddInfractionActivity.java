package com.sternerlearn;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class AddInfractionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_infraction);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_infraction, menu);
		return true;
	}
	
    public void handleSubmit
		(
		View aView
		)
	{
		//TODO
	} 	

}
