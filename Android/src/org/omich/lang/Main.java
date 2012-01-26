package org.omich.lang;



import org.omich.lang.R;
import org.omich.lang.words.WordsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



public class Main extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	
	private static final String PREFES_NAME = "MyPrefsFile";
	
	private   EditText textLogin;
	private   EditText textPassword;
	
	private AuthSettigs settigs;
	
	private Intent intent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textLogin = (EditText) findViewById(R.id.editLogin);
    	textPassword = (EditText) findViewById(R.id.editPassword);
       
    	settigs = new AuthSettigs(PREFES_NAME,this);
    	
    	textLogin.setText(settigs.getLogin());
    	textPassword.setText(settigs.getPassword());
    	
    	Button exitButton = (Button) findViewById(R.id.button1);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        
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
    				String login = textLogin.getText().toString();
    				String password = textPassword.getText().toString();
    				settigs.saveAuthData(login, password);
    				
					break;
    				
    			case R.id.button1:
    				finish();
    				break;
    		}
    }
}