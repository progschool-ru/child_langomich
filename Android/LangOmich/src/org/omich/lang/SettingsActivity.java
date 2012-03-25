package org.omich.lang;

import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONParser;

import com.ccg.util.JavaString;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class SettingsActivity extends LangOmichActivity implements OnClickListener{
	
	private static final int REQUEST_LANGUAGES = 0;
	private static final int REQUEST_NUMBER_OF_WORDS = 1;
	private static final int REQUEST_LOGIN = 2;
	private static final int REQUEST_PASSWORD = 3; 
	
	private TextView language;
	private TextView numberOfWords;
	private TextView login;
	private TextView password;
	
	private String login_s;
	private String password_s;
	
	ImageButton selectLanguages;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settigs);
		
		
		language = (TextView) findViewById(R.id.language);
		String languages_s = lSettigs.getLanguageName();
		language.setText(languages_s);
		
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
		setInputType();
		
		ImageButton selectNumberButton = (ImageButton) findViewById(R.id.number_button);
		selectNumberButton.setOnClickListener(this);
	    selectLanguages = (ImageButton) findViewById(R.id.select_language);
		selectLanguages.setOnClickListener(this);
		
		updateLangButton();
		checkAuthData();
		
		lSettigs.edit();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	public void onClick(View v){
		
		switch(v.getId()){
			
			case R.id.select_language:
				startSelectLanguageDialog();
				break;	
			case R.id.number_button:
				startSelectNuberOfWordsDialog();
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
				                                   
				case REQUEST_LANGUAGES:
					String language_s = data.getExtras().getString(LangOmichSettings.LANGUAGE_NAME);
					int language_id = data.getExtras().getInt(LangOmichSettings.LANGUAGE_ID);
					lSettigs.saveLanguage(language_s, language_id);
					language.setText(language_s);
					break;
				
				case REQUEST_NUMBER_OF_WORDS:
					String number = data.getExtras().getString(LangOmichSettings.NUMBER_WORDS);
					numberOfWords.setText(number);
					lSettigs.saveNumberWords(Integer.parseInt(number));
					break;
					
				case REQUEST_LOGIN:
					login_s = data.getExtras().getString(LangOmichSettings.LOGIN);
					lSettigs.saveLogin(login_s);
					login.setText(login_s);
					checkAuthData();
					break;
					
				case REQUEST_PASSWORD:
					password_s = data.getExtras().getString(LangOmichSettings.PASSWORD);
					boolean show  = data.getExtras().getBoolean(LangOmichSettings.SHOW, LangOmichSettings.DEFAULT_SHOW);
					lSettigs.saveShow(show);
					lSettigs.savePassword(password_s);
					password.setText(password_s);
				
						setInputType(); 
						checkAuthData();
				
					break;
				
				
			}
		}
	}
	
	private void startLoginDialog(){
		
		Intent loginIntent = new Intent(this, LoginDialogActivity.class);
		loginIntent.putExtra(LangOmichSettings.LOGIN, login_s); 
		startActivityForResult(loginIntent, REQUEST_LOGIN);
	}
	
	private void startPasswordDialog(){
		Intent passwordIntent = new Intent(this, PasswordDialogActivity.class);
		
		passwordIntent.putExtra(LangOmichSettings.PASSWORD, password_s);
		boolean show = lSettigs.getShow();
		passwordIntent.putExtra(LangOmichSettings.SHOW, show);
		
		startActivityForResult(passwordIntent, REQUEST_PASSWORD);
		
	}
	
	private void startSelectLanguageDialog(){
		Intent languageIntent = new Intent(this, LanguageDialogActivity.class);
		startActivityForResult(languageIntent, REQUEST_LANGUAGES);
	}
	
	private void startSelectNuberOfWordsDialog(){
		Intent numberIntent = new Intent(this, WordsNumberActivity.class);
		startActivityForResult(numberIntent, REQUEST_NUMBER_OF_WORDS);
	}
	
	private void checkAuthData(){
		if(login_s.equals(LangOmichSettings.DEFAULT_LOGIN) || password_s.equals(LangOmichSettings.DEFAULT_PASSWORD)){
			login.setTextColor(Color.GRAY);
			password.setTextColor(Color.GRAY);
			return;
		}
		if(!isNetworkAvailable()){
			login.setTextColor(Color.GRAY);
			password.setTextColor(Color.GRAY);
			return;
		}
		
		AsyncAuth auth = new AsyncAuth();
		auth.execute(new Void [] {});
	}
	
	private void setInputType(){
		if(!password_s.equals(LangOmichSettings.DEFAULT_PASSWORD)){
			password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}else{
			password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
	}
	
	@Override
	public void updateInterfaseAfteSync(){
		super.updateInterfaseAfteSync();
		updateLangButton();
	}
	
	private void updateLangButton(){
		langData.open();
		if(langData.isEmpty()) {
			selectLanguages.setEnabled(false);
		}else{
			selectLanguages.setEnabled(true);
		}
		langData.close();
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