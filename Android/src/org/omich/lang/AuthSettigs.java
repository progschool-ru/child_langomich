package org.omich.lang;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.omich.lang.Constants;

public class AuthSettigs {
	

 
	private SharedPreferences settigs;

	public AuthSettigs(Activity act, String PREFES_NAME){
		settigs = act.getSharedPreferences(PREFES_NAME, 0);
	}
	
	public boolean SaveAuthData(String login, String password){
		if(!login.equals(Constants.EMPTY_STRING) && !password.equals(Constants.EMPTY_STRING)){
			Editor edit = settigs.edit();
			edit.putString(Constants.STR_LOGIN, login);
			edit.putString(Constants.STR_PASSWORD, password);
			edit.commit();
			return true;
		}
		return false;
	}
	
	public String getLogin(){
		return settigs.getString(Constants.STR_LOGIN, Constants.EMPTY_STRING);
	}
	
	public String getPassword(){
		return settigs.getString(Constants.STR_PASSWORD, Constants.EMPTY_STRING);
	}
	
	public void clear(){
		settigs.edit().clear();
		settigs.edit().commit();
	}
}
