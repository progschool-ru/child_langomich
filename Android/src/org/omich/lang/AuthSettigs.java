package org.omich.lang;

import android.app.Activity;
import android.content.SharedPreferences;

public class AuthSettigs {
	
	private final String login = "login";
	private final String password = "password";
	private final String clearString = "";
	
	private SharedPreferences settings;
	
	public AuthSettigs(String PREFER_NAME, Activity act){
		settings = act.getSharedPreferences(PREFER_NAME,0);
	}
	
	public boolean saveAuthData(String _login, String _password){
		if((!_login.equals("")) && (!_password.equals(""))){
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(login, _login);
			editor.putString(password, _password);
			editor.commit();
		return true;
		}else{
			return false;
		}
	}
	
	public void clear(){
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
	public String getLogin(){
		return settings.getString(login, clearString);
	}
	
	public String getPassword(){
		return settings.getString(password, clearString);
	}
}
