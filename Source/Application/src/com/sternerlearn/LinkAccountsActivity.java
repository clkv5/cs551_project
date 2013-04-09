package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class LinkAccountsActivity extends Activity {

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
			    /* Start task to link accounts  */
	    		AsyncCallLink task = new AsyncCallLink();
		    	task.execute();
	    	
	    	}  // onClick()
	    });  //setOnClickListener()
	}
	

    private class AsyncCallLink extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... values) {
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
	        
	        /* Log to Locations database */
	        try {
	        	SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.ACCOUNT_URL, Types.ACCOUNT_LINK, paramList );
				mResponse = envelope.getResponse().toString();
			} catch (SoapFault e) {
				mResponse = "SOAP fault on return";
			}
	        
	        /* Notify parent */
	        TextView textView = (TextView)findViewById( R.id.linkAccountsTextView );
	        if( mResponse == "Accounts linked successfully." )
	        {
	        	textView.setText(mResponse);
	        }
	        else
	        {
	        	textView.setText("Error linking accounts");
	        }
	        
	        return null;
        }

        @Override
        protected void onPostExecute(Void value){}

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }
}
