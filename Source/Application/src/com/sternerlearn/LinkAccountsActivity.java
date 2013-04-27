package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class LinkAccountsActivity extends Activity {
	
	/* Constants */
	private final String ACCOUNT_LINK_RETURN = "Accounts linked successfully";
	
	/* XML widget declarations */
	private Button   submitButton;
	private EditText studentEmail;
	private EditText studentPass;
	private TextView textView;
	
	/* Debug variables */
	public static String mResponse = "Initial";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Set content view */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link_accounts);
		
		/* Make widget instances */
	    submitButton = (Button)findViewById( R.id.linkAccountsButton );
	    studentEmail = (EditText)findViewById( R.id.editStudentEmail );
	    studentPass = (EditText)findViewById( R.id.editStudentPassword );
	    textView  = (TextView)findViewById( R.id.linkAccountsTextView );
		
		/* Display page based on linked ID */
		Account acct = SharedData.getInstance().getAccount();
		if( acct.mLinkedId != -1 )
		{
			textView.setText("Account is already linked.");
			submitButton.setVisibility(View.GONE);
			studentEmail.setVisibility(View.GONE);
			studentPass.setVisibility(View.GONE);
		}
		else
		{
			textView.setText("Please enter your child's account info");
			createButtonListener();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.link_accounts, menu);
		return true;
	}
	
	
	private void createButtonListener()
	{
	    /* Create submit button event */
	    submitButton.setOnClickListener( new View.OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		mResponse = "Initial";
			    /* Start task to link accounts  */
	    		AsyncCallLink task = new AsyncCallLink();
		    	task.execute();
		    	
	    	}  // onClick()
	    });  //setOnClickListener()
	}
	
	private boolean attemptLink()
	{
		boolean success;
		
    	/* Get input text */
    	String email = ((EditText)findViewById( R.id.editStudentEmail )).getText().toString();
    	String pass = ((EditText)findViewById( R.id.editStudentPassword )).getText().toString();	
    	
    	/* Get parent account credentials */
    	Account acct = SharedData.getInstance().getAccount();
    	String parentEmail = acct.mEmailAddress;
        String parentPass = acct.mPassword;
    	
    	/* Create list of web method parameters */
        List<PropertyWrapper> paramList = new ArrayList<PropertyWrapper>();
        paramList.add( new PropertyWrapper( "parentEmail" , parentEmail ) );
        paramList.add( new PropertyWrapper( "parentPassword" , parentPass ) );
        paramList.add( new PropertyWrapper( "studentEmail" , email ) );
        paramList.add( new PropertyWrapper( "studentPassword" , pass ) );
        
        /* Set Linked IDs in both accounts */
        try {
        	SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.ACCOUNT_URL, Types.ACCOUNT_LINK, paramList );
			mResponse = envelope.getResponse().toString();
			
			if( mResponse == ACCOUNT_LINK_RETURN + "  Please login again." )
			{
				success = true;
			}
			else
			{
				success = false;
			}
		} catch (SoapFault e) {
			mResponse = "SOAP fault on return";
			success = false;
		}
        
        if( success )
        {
        	Account studentAccount = new Account(email, pass);
        	studentAccount.mLinkedAccount = SharedData.getInstance().getAccount();
        	
    		// Set the account to the student's account so that future operations will be done using the student's info
        	// This is pretty hacky but I really don't want to redo more things
    		SharedData.getInstance().setAccount( studentAccount );
	    	
    		// Save the student's login information
    		SharedPreferences settings = getSharedPreferences(Types.PREFS_FILE, MODE_PRIVATE);
    		
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putString(Types.STUDENT_LOGIN_KEY, email );
    		editor.putString(Types.STUDENT_PW_KEY, pass );
    		
    		editor.commit();    		
        }
        return success;
	}
	
	private void finishLink(boolean success)
	{
		textView.setText(mResponse);
	}

    private class AsyncCallLink extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... values) {
        	return attemptLink();
        }

        @Override
        protected void onPostExecute(Boolean value)
        {
        	finishLink(value);        	
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }
}
