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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        settigs = new AuthSettigs(this, Constants.PREFERS_NAME);
        
        editLogin = (EditText) findViewById(R.id.login);
        editPassword = (EditText) findViewById(R.id.password);
        
        next = (Button) findViewById(R.id.nextButton);
        exit = (Button) findViewById(R.id.exitButton);
        
        editLogin.setText(settigs.getLogin());
        editPassword.setText(settigs.getPassword());
        
        next.setOnClickListener(this);
        exit.setOnClickListener(this);
    }
    
    public void onClick(View g){
    	switch(g.getId()){
    	
    		case R.id.nextButton:
    			String login = editLogin.getText().toString();
    			String password = editPassword.getText().toString();
    			Intent intent = new Intent(MainActivity.this, WordsActivity.class);
    			if(settigs.SaveAuthData(login, password)){
    				intent.putExtra(Constants.STR_LOGIN, login);
    				intent.putExtra(Constants.STR_PASSWORD, password );
    				startActivity(intent);
    			}
    			break;
    		case R.id.exitButton:
    			finish();
    			break;
    	}
    }
}