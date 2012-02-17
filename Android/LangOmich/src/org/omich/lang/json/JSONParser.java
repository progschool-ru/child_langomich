package org.omich.lang.json;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.words.Language;

public class JSONParser {
	private static final String LANGUAGES = "languages";
	private static final String SUCCESS = "success";
	private static final String LAST_CONNECTION = "lastConnection";
	
	public static  List<Language> parseLanguages(String jString, long lastConnection) throws JSONException{
		
		List<Language> languages = null;
		
		JSONObject jObject = new JSONObject(jString);
		
		if(jObject.has(LAST_CONNECTION)){
			lastConnection = jObject.getLong(LAST_CONNECTION);
		}else{
			lastConnection = 0;
		}
		
		boolean success = jObject.getBoolean(SUCCESS);
		
		if (success){
			JSONArray jLanguages = jObject.getJSONArray(LANGUAGES);
			languages = JSONLanguages.parse(jLanguages);
			
		}
		
		return languages;
	}
	
	public static boolean parseAuth(String jString) throws JSONException{
		
		JSONObject jObject = new JSONObject(jString);
		
		boolean success = jObject.getBoolean(SUCCESS);
		return success;
	}
}
