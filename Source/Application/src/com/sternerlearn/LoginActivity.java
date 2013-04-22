package com.sternerlearn;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void handleLogin
    	(
    	View aView
    	)
    {
    	AsyncCallLogin task = new AsyncCallLogin();
    	task.execute();
    }  
    
    public void finishLogin()
    {
    	Account acct = SharedData.getInstance().getAccount();
    	
    	boolean error = false;
    
    	if( acct.mValid )
    	{
			if( Types.AccountType.PARENT.ordinal() == acct.mAccountType )
			{
				startActivity(new Intent(this, MainMenu.class));	
			}
			else if( Types.AccountType.STAFF.ordinal() == acct.mAccountType )
			{
				startActivity(new Intent(this, TeacherMainMenu.class));	
			}
			else if( Types.AccountType.STUDENT.ordinal() == acct.mAccountType )
			{
				startActivity(new Intent(this, StudentMainMenu.class));
				startService(new Intent(this, GPSReceiver.class));
			}
			else
			{
				error = true;			
			}
    	}
    	else
    	{
    		error = true;
    	}
    	
    	if( error )
    	{
    		CharSequence text = "Error logging in";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
    	}
    }
    
    public Account login()
    {
    	// Need to attempt to log them in
    	// If it succeeds, then send the account type to the MainMenu
    	String email = ((EditText)findViewById( R.id.editEmailAddress )).getText().toString();
    	String password = ((EditText)findViewById( R.id.editPassword )).getText().toString();
    	
        Account account = new Account( email, password ); 
    	
    	return account;
    }
    
    public void handleRegister( View aView )
    {
    	startActivity(new Intent(this, RegisterActivity.class));
    }
    
    private class AsyncCallLogin extends AsyncTask<Void, Void, Account> {
        @Override
        protected Account doInBackground(Void... params) {
            return login();
        }

        @Override
        protected void onPostExecute(Account result) {
    		SharedData.getInstance().setAccount(result);
    		finishLogin();        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }     
}
