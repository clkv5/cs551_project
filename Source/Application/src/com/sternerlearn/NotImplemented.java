package com.sternerlearn;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NotImplemented extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_not_implemented);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.not_implemented, menu);
		return true;
	}

}
