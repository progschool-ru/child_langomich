package org.omich.lang.json;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONAuthData {
	
	private static final String SUCCESS = "success";
	
	private boolean success;

	public JSONAuthData(String jString) throws JSONException{
		
		JSONObject jObject= new JSONObject(jString);
		
		success = jObject.getBoolean(SUCCESS);
	}
	
	public boolean getSuccess(){
		return success;
	}
}
