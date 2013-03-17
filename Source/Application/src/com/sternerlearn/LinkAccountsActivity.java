package com.sternerlearn;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

// TODO: Make this a functioning page to link two accounts
public class LinkAccountsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link_accounts);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.link_accounts, menu);
		return true;
	}

}
