package com.sternerlearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

    public void handleRegister( View aView )
    {
    	// Try to register them. If successful just take them to the MainMenu
    	// If not then report errors
    	
    	AsyncCallRegister task = new AsyncCallRegister();
    	task.execute();
    }
    
    public Account register()
    {
    	String email = ((EditText)findViewById( R.id.emailAddress )).getText().toString();
    	String password = ((EditText)findViewById( R.id.password )).getText().toString();  
    	String passwordRepeat = ((EditText)findViewById( R.id.passwordRepeat )).getText().toString();
    	String realName = ((EditText)findViewById( R.id.realName )).getText().toString();
    	
    	RadioGroup group = ((RadioGroup)findViewById( R.id.radioGroup1 ));
    	View button = findViewById(group.getCheckedRadioButtonId());
    	int type = group.indexOfChild(button);
    	
    	Account account = new Account( email, password, passwordRepeat, realName, type );
    	return account;
    }
    
    public void finishRegister()
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
    		CharSequence text = "Error registering";
    		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    		toast.show();
    	}    	
    }
    
    private class AsyncCallRegister extends AsyncTask<Void, Void, Account> {
        @Override
        protected Account doInBackground(Void... params) {
            return register();
        }

        @Override
        protected void onPostExecute(Account result) {
        	SharedData.getInstance().setAccount(result);
    		finishRegister();        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }        
}
