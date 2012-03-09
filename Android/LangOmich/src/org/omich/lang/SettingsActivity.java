package org.omich.lang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SettingsActivity extends FragmentActivity implements OnClickListener{
	
	public static final String SETTINGS_NAME = "langOmich_settings";
	
	private static final int DIALOG_SELECT_LANGUAGE = 0;
	private static final int DIALOG_SET_NUMBER_OF_WORDS = 1;
	
	private static final int REQUEST_LOGIN = 2;
	private static final int REQUEST_PASSWORD = 3; 

	private static ProgressDialog dialog;
	
	private TextView language;
	private TextView numberOfWords;
	private TextView login;
	private TextView password;
	
	private String login_s;
	private String password_s;
	
	private LangOmichSettings lSettigs;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settigs);
		
		lSettigs = new LangOmichSettings(this, SETTINGS_NAME);
		
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
					break;
			}
		}
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	  MenuItem item = menu.add(0, android.R.id.copy, 0, "Menu");

          final int twentyDp = (int) (20 * getResources().getDisplayMetrics().density);

          TypedArray a = getTheme().obtainStyledAttributes(R.styleable.SherlockTheme);
          final int abHeight = a.getLayoutDimension(R.styleable.SherlockTheme_abHeight, LayoutParams.FILL_PARENT);
          a.recycle();

          LinearLayout l = new LinearLayout(this);
          l.setPadding(twentyDp, 0, twentyDp, 0);

          TextView tv = new TextView(this);
          tv.setText("Sync");
          tv.setGravity(Gravity.CENTER);
          tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, abHeight));
          l.addView(tv);

          l.setOnClickListener(new View.OnClickListener() {
            
              public void onClick(View v) {
            	showSyncProgressBar();
              }
          });

          item.setActionView(l);
          item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

          return super.onCreateOptionsMenu(menu);
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
	
	private void showSyncProgressBar(){

		 dialog = ProgressDialog.show(this,"","Синхронизация"	);
	}
}