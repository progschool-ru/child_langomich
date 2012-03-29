package org.omich.lang;

import android.content.Context;
import android.content.SharedPreferences;

public class LangOmichSettings {
	
	private static final String EMPTY_STRING = "";
	private static final long NULL = 0;
	
	public static final String LOGIN = "login";
	public static final String DEFAULT_LOGIN = "Введите логин";
	
	public static final String PASSWORD = "password";
	public static final String DEFAULT_PASSWORD = "Введите пароль";
	public static final String SHOW = "show";
	public static final boolean DEFAULT_SHOW = false;
	
	public static final String LANGUAGE_ID = "language_id";
	public static final String LANGUAGE_NAME = "language_name";
	
	public static final String NUMBER_WORDS = "number_words";
	public static final int DEFAULT_NUMBER_WORDS = 0;
	
	public static final String LAST_CONNECTION = "last_conn";
	
	private SharedPreferences settings;
	private SharedPreferences.Editor edit;
	
	public LangOmichSettings(Context context, String PREFES_NAME){
		settings = context.getSharedPreferences(PREFES_NAME, 0);
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
	
	public void saveLanguage(String name, int id){
		edit.putString(LANGUAGE_NAME, name);
		edit.putInt(LANGUAGE_ID, id);
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
	
	public void saveShow(boolean show){
		edit.putBoolean(SHOW, show);
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
	
	public int getLanguageId(){
		return settings.getInt(LANGUAGE_ID, 0);
	}
	
	public int getNumberWords(){
		return settings.getInt(NUMBER_WORDS, DEFAULT_NUMBER_WORDS);
	}
	
	public long getLastConnect(){
		return settings.getLong(LAST_CONNECTION, NULL);
	}
	
	public boolean getShow(){
		return settings.getBoolean(SHOW, DEFAULT_SHOW);
	}
	
	public void clear(){
		edit.clear();
		edit.commit();
	}
	
}
