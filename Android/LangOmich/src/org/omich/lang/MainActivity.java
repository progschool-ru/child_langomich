package org.omich.lang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
   
	
    private AuthSettigs settigs;
    private EditText editLogin; 
    private EditText editPassword;
    private Button next;
    private Button exit;
    private Button statrTestActivity;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        settigs = new AuthSettigs(this, Constants.PREFERS_NAME);
        
        editLogin = (EditText) findViewById(R.id.login);
        editPassword = (EditText) findViewById(R.id.password);
        
        next = (Button) findViewById(R.id.nextButton);
        exit = (Button) findViewById(R.id.exitButton);
        statrTestActivity = (Button) findViewById(R.id.button1);
        
        
        editLogin.setText(settigs.getLogin());
        editPassword.setText(settigs.getPassword());
        
        statrTestActivity.setOnClickListener(this);
        next.setOnClickListener(this);
        exit.setOnClickListener(this);
    }
    
    public void onClick(View g){
    	
    	String login = editLogin.getText().toString();
		String password = editPassword.getText().toString();
    	
    	switch(g.getId()){
    	
    		case R.id.nextButton:
    			
    			Intent intent = new Intent(MainActivity.this, WordsActivity.class);
    			if(settigs.saveAuthData(login, password)){
    				intent.putExtra(Constants.STR_LOGIN, login);
    				intent.putExtra(Constants.STR_PASSWORD, password );
    				startActivity(intent);
    			}
    			break;
    		case R.id.exitButton:
    			finish();
    			break;
    		case R.id.button1:
    			Intent intent1 = new Intent(MainActivity.this, TestActivity.class);
    			intent1.putExtra(Constants.STR_LOGIN, login);
    			intent1.putExtra(Constants.STR_PASSWORD, password);
    			startActivity(intent1);
    	}
    }
}