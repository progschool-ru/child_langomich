package org.omich.lang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class SettingsActivity extends Activity implements OnClickListener{
	
	private static final int DIALOG_SELECT_LANGUAGE = 0;
	private static final int DIALOG_SET_NUMBER_OF_WORDS = 1;
	private static final int DIALOG_SET_LOGIN = 2;
	private static final int DIALOG_SET_PASSWORD =3;
	

	
	private TextView language;
	private TextView numberOfWords;
	private TextView login;
	private TextView password;
	
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
				showDialog(DIALOG_SET_LOGIN);
				break;
			case R.id.password:
				showDialog(DIALOG_SET_PASSWORD);
				break;
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
			
			case DIALOG_SET_LOGIN:{
				
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				
					View layout = inflater.inflate(R.layout.login, null);
				
					bulder.setView(layout);
				
					final EditText editLogin = (EditText) layout.findViewById(R.id.login_edit);
					editLogin.setText(login.getText());
					Button cancel = (Button) layout.findViewById(R.id.login_cancel);
					Button ok = (Button) layout.findViewById(R.id.login_ok);
					
					editLogin.setOnClickListener( new OnClickListener() {
						public void onClick(View v) {
							((EditText) v).setText("");
						}
					});
					
					ok.setOnClickListener( new OnClickListener() {
						public void onClick(View v) {
							String l = editLogin.getText().toString();
							login.setText(l);
							dismissDialog(DIALOG_SET_LOGIN);
						}
					});
				
					cancel.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dismissDialog(DIALOG_SET_LOGIN);
						}
					});
				}
			
			break;
			
			case DIALOG_SET_PASSWORD:{
				
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				
				View layout = inflater.inflate(R.layout.password, null);
			
				bulder.setView(layout);
			
				final EditText editPassword = (EditText) layout.findViewById(R.id.passwordEdit);
				editPassword.setText(password.getText());
				final CheckBox show = (CheckBox) layout.findViewById(R.id.show);
				Button cancel = (Button) layout.findViewById(R.id.password_cancel);
				Button ok = (Button) layout.findViewById(R.id.password_ok);
				
				show.setOnClickListener( new OnClickListener() {
					public void onClick(View v) {
						if(((CheckBox) v).isChecked()){
							editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							
						}else{
							editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
					}
				});
				
				ok.setOnClickListener( new OnClickListener() {
					public void onClick(View v) {
						String l = editPassword.getText().toString();
						password.setText(l);
						dismissDialog(DIALOG_SET_PASSWORD);
					}
				});
			
				cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismissDialog(DIALOG_SET_PASSWORD);
					}
				});
				
			}
					
			break;
		}
		
		return bulder.create();
	}

}
