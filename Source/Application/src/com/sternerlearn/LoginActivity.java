package com.sternerlearn;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

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
    	// Need to attempt to log them in
    	// If it succeeds, then send the account type to the MainMenu
    	// TODO: Need to retrieve account type when logging in
    	// If it fails give them a Toast
    	
    	startActivity(new Intent(this, ParentMainMenu.class));
    }
    
    public void handleRegister( View aView )
    {
    	startActivity(new Intent(this, RegisterActivity.class));
    }
}
