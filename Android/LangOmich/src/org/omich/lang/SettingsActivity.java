package org.omich.lang;

import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONParser;

import com.ccg.util.JavaString;

import android.R.bool;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class SettingsActivity extends LangOmichActivity implements OnClickListener{
	
	
	
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
		int number = lSettigs.getNumberWords();
		if(number != LangOmichSettings.DEFAULT_NUMBER_WORDS){
			numberOfWords.setText(Integer.toString(number));
		}
	
		login = (TextView) findViewById(R.id.login);
		login_s = lSettigs.getLogin();
		login.setText(login_s);
		login.setOnClickListener(this);
		
		password =(TextView) findViewById(R.id.password);
		password_s = lSettigs.getPassword();
		password.setText(password_s);
		password.setOnClickListener(this);
		
		ImageButton selectNumberButton = (ImageButton) findViewById(R.id.number_button);
		selectNumberButton.setOnClickListener(this);
		
		login_s = login.getText().toString();
		password_s = password.getText().toString();
		
		lSettigs.edit();
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
					lSettigs.saveLogin(login_s);
					login.setText(login_s);
					break;
				case REQUEST_PASSWORD:
					password_s = data.getExtras().getString(PasswordDialogActivity.PASSWORD);
					lSettigs.savePassword(password_s);
					password.setText(password_s);
					AsyncAuth auth = new AsyncAuth();
					auth.execute(new Void [] {});
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
								//numberOfWords.setText(item[which]);
							saveNumberOfWords(which);
						}
					});
				
				break;
			
		}
		return bulder.create();
	}
	
	private void startLoginDialog(){
		Intent loginIntent = new Intent(this, LoginDialogActivity.class);
		 
		 if(login_s != LangOmichSettings.DEFAULT_LOGIN){
			loginIntent.putExtra(LoginDialogActivity.LOGIN, login_s); 
		 }
		 
		 startActivityForResult(loginIntent, REQUEST_LOGIN);
		
	}
	private void startPasswordDialog(){
		Intent passwordIntent = new Intent(this, PasswordDialogActivity.class);
		
		if(password_s != LangOmichSettings.DEFAULT_PASSWORD){
			passwordIntent.putExtra(PasswordDialogActivity.PASSWORD, password_s);
		}
		
		startActivityForResult(passwordIntent, REQUEST_PASSWORD);
		
	}
	
	private void saveNumberOfWords(int number){
		lSettigs.saveNumberWords(number+1);
		numberOfWords.setText(Integer.toString(number+1));
	}
	
	
	private class AsyncAuth extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params){
			SmdClient client = new SmdClient();
			
			boolean authres;
			
			try {
				 String result = client.auth(login_s, password_s);
				 String jString = JavaString.decode(result);
				 authres = JSONParser.parseAuth(jString);
			} catch (Exception e) {
				authres = false;
			}
		
			return authres;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			if(result){
				login.setTextColor(Color.GREEN);
				password.setTextColor(Color.GREEN);
			}else{
				login.setTextColor(Color.RED);
				password.setTextColor(Color.RED);
			}
		}
	}
}