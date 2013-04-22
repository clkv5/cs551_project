package com.sternerlearn;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class DisplayMessagesActivity extends ListActivity {

	private MessageAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_messages);
		
		/* Setup list adapter */
		mAdapter = new MessageAdapter(this, null);
		setListAdapter(mAdapter);	
		
		/* Get student account with message list */
		Student stu = SharedData.getInstance().getStudent();
		
		/* Fill list */
		mAdapter.setMessages( stu.mMessages );
		mAdapter.notifyDataSetChanged();
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_messages, menu);
		return true;
	}

}
