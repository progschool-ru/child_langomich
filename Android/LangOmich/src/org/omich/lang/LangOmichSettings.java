package org.omich.lang;

import android.app.Activity;
import android.content.SharedPreferences;

public class LangOmichSettings {
	
	private static final String EMPTY_STRING = "";
	private static final int  INULL = 0;
	private static final long LNULL = 0;
	
	public static final String LOGIN = "login";
	public static final String DEFAULT_LOGIN = "Введите логин";
	
	public static final String PASSWORD = "password";
	public static final String DEFAULT_PASSWORD = "Введите пароль";
	
	public static final String LANGUAGE_ID = "language_id";
	public static final String LANGUAGE_NAME = "language_name";
	
	public static final String NUMBER_WORDS = "number_words";
	public static final int DEFAULT_NUMBER_WORDS = 0;
	
	public static final String LAST_CONNECTION = "last_conn";
 
	private SharedPreferences settings;
	private SharedPreferences.Editor edit;
	
	public LangOmichSettings(Activity act, String PREFES_NAME){
		settings = act.getSharedPreferences(PREFES_NAME, 0);
	}
	
	
	public void edit(){
		edit = settings.edit();
	}
	
	public void saveLogin(String login){
		edit.putString(LOGIN, login);
		edit.commit();
	}
	
	public void savePassword(String password){
		edit.putString(PASSWORD, password);
		edit.commit();
	}
	
	public void saveLanguage(String name, String id){
		edit.putString(LANGUAGE_NAME, name);
		edit.putString(LANGUAGE_ID, id);
		edit.commit();
	}
	
	public void saveNumberWords(int number){
		edit.putInt(NUMBER_WORDS, number);
		edit.commit();
	}
	
	public void saveLastConnection(long last_conn){
		edit.putLong(LAST_CONNECTION, last_conn);
		edit.commit();
	}
	
	public String getLogin(){
		return settings.getString(LOGIN, DEFAULT_LOGIN);
	}
	
	public String getPassword(){
		return settings.getString(PASSWORD, DEFAULT_PASSWORD);
	}
	
	public String getLanguageName(){
		return settings.getString(LANGUAGE_NAME, EMPTY_STRING);
	}
	
	public String getLanguageId(){
		return settings.getString(LANGUAGE_ID, EMPTY_STRING);
	}
	
	public int getNumberWords(){
		return settings.getInt(NUMBER_WORDS, DEFAULT_NUMBER_WORDS);
	}
	
	public long getLastConnect(){
		return settings.getLong(LAST_CONNECTION, LNULL);
	}
	
	public void clear(){
		edit.clear();
		edit.commit();
	}
	
}
