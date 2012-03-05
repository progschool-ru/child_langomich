package org.omich.lang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class SettingsActivity extends Activity implements OnClickListener{
	
	private static final int DIALOG_SELECT_LANGUAGE = 0;
	private static final int DIALOG_SET_NUMBER_OF_WORDS = 1;
	
	private static final int REQUEST_LOGIN = 2;
	private static final int REQUEST_PASSWORD = 3; 

	
	private TextView language;
	private TextView numberOfWords;
	private TextView login;
	private TextView password;
	
	private String login_s;
	private String password_s;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settigs);
		
		numberOfWords = (TextView) findViewById(R.id.number);
		login = (TextView) findViewById(R.id.login);
		password =(TextView) findViewById(R.id.password);
		
		ImageButton selectNumberButton = (ImageButton) findViewById(R.id.number_button);
		selectNumberButton.setOnClickListener(this);
		login.setOnClickListener(this);
		password.setOnClickListener(this);
		
		login_s = login.getText().toString();
		password_s = password.getText().toString();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	public void onClick(View v){
		
		switch(v.getId()){
			
			case R.id.number_button:
				showDialog(DIALOG_SET_NUMBER_OF_WORDS);
				break;
			case R.id.login:
				startLoginDialog();
				break;
			case R.id.password:
				startPasswordDialog();
				break;
		}
	}
	
	@Override
	protected void onActivityResult( int requesCode, int resultCode, Intent data){
		if( resultCode  ==  RESULT_OK){
			
			switch(requesCode){
				case REQUEST_LOGIN:
					login_s = data.getExtras().getString(LoginDialogActivity.LOGIN);
					login.setText(login_s);
					break;
				case REQUEST_PASSWORD:
					password_s = data.getExtras().getString(PasswordDialogActivity.PASSWORD);
					password.setText(password_s);
					break;
			}
		}
	}
	
	protected Dialog onCreateDialog(int id){

		AlertDialog.Builder bulder = new AlertDialog.Builder(this);
		
		switch(id){
		
			case DIALOG_SELECT_LANGUAGE:
				break;
			
			case DIALOG_SET_NUMBER_OF_WORDS:
					
				final CharSequence[] item = {"1","2","3","4","5","6"};
					
				    bulder.setTitle("Pick a number of words");
					
					bulder.setItems(item, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							numberOfWords.setText(item[which]);
						}
					});
				
				break;
			
		}
		return bulder.create();
	}
	
	private void startLoginDialog(){
		Intent loginIntent = new Intent(this, LoginDialogActivity.class);
		 
		 if(login_s != getResources().getString(R.string.input_login)){
			loginIntent.putExtra(LoginDialogActivity.LOGIN, login_s); 
		 }
		 
		 startActivityForResult(loginIntent, REQUEST_LOGIN);
		
	}
	
	private void startPasswordDialog(){
		Intent passwordIntent = new Intent(this, PasswordDialogActivity.class);
		
		if(password_s != getResources().getString(R.string.input_password)){
			passwordIntent.putExtra(PasswordDialogActivity.PASSWORD, password_s);
		}
		
		startActivityForResult(passwordIntent, REQUEST_PASSWORD);
		
	}
	

}