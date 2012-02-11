package org.omich.lang;

import org.json.JSONException;
import org.json.JSONObject;


public class AuthResParser {
	
	private static final String USER = "user";
	private static final String SUCCESS = "success";
	private static final String LOGIN = "login";
	private static final String USER_ID = "userId";
	
	private Boolean success;
	private String login;
	private String userId;
	
	public AuthResParser(String jString) throws JSONException{
		JSONObject jObject = new JSONObject(jString);
		
		success = jObject.getBoolean(SUCCESS);
		
		if(success){
		 JSONObject user = jObject.getJSONObject(USER);
		 userId = user.getString(USER_ID);
		 login = user.getString(LOGIN);
		}
	}
	
	public Boolean getSuccess(){
		return success;
	}
	
	//пока не понимаю зачем нужны следующие методы, но все же
	public String getLogin(){
		return login;
	}
	
	public String getUserID(){
		return userId;
	}
}
