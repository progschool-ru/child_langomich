package net.langomich;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	
	private static final String PREFES_NAME = "MyPrefsFile";
	private   EditText textLogin;
	private   EditText textPassword; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textLogin = (EditText) findViewById(R.id.editLogin);
    	textPassword = (EditText) findViewById(R.id.editPassword);
       
    	Button exitButton = (Button) findViewById(R.id.button1);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        
        SharedPreferences settings = getSharedPreferences(PREFES_NAME, 0);
        
        textLogin.setText(settings.getString("login", ""));
        textPassword.setText(settings.getString("password", ""));
        
        saveButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	
    }
    
    public void onClick(View v){
    		switch(v.getId()){
    			case R.id.saveButton:
    				
    				String login =  textLogin.getText().toString();;
					String password = textPassword.toString();
					
					if(!(login.equals("")) && !(password.equals(""))){
						SharedPreferences settings = getSharedPreferences(PREFES_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("login", login);
						editor.putString("password", password);
						editor.commit();
					}
    				break;
    				
    			case R.id.button1:
    				finish();
    				break;
    		}
    }
}