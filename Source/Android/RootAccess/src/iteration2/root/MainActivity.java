package iteration2.root;

// General imports
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.io.IOException;

// Toast message imports
import android.widget.Toast;
import android.content.Context;

// Root-specific imports
import java.io.DataOutputStream;


public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Process p;
		try {
			p = Runtime.getRuntime().exec("su"); 
			
			   DataOutputStream os = new DataOutputStream(p.getOutputStream());  
			   os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  
			
			   os.writeBytes("exit\n");  
			   os.flush();  
			   
			   try {  
				      p.waitFor();  
				           if (p.exitValue() != 255) {  
				        	   showMsg("root");  
				           }  
				           else {  
				        	   showMsg("not root 1");  
				           }  
			   } catch (InterruptedException e) {  
				   showMsg("not root 2");  
				   }  
			   
		} catch(IOException e) {
			showMsg("not root 3");
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	protected void showMsg(String msg) {
		Toast toastMessage;
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;
		
		toastMessage = Toast.makeText(context, text, duration);
		toastMessage.show();
		
	}

}
