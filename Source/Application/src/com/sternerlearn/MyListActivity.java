package com.sternerlearn;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MyListActivity extends ListActivity {
	
	protected String[] TITLES;
	
	protected ArrayList<String> mListItems = new ArrayList<String>();
	protected ArrayAdapter<String> mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, mListItems );
		setListAdapter( mAdapter );
		addItems();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_main_menu, menu);
		return true;
	}
	
	public void addItems()
	{
		for( int i = 0; i < TITLES.length; i++ )
		{
			mListItems.add( TITLES[i]);
		}
		
		mAdapter.notifyDataSetChanged();
	}

}
