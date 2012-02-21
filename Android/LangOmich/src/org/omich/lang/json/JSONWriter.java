package org.omich.lang.json;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.words.Language;

public class JSONWriter {
	
	private static final String LANGUAGES = "languages";
	private static final String LAST_CONNECTION = "lastConnection";
	
	
	public static String toJSON(long lastConnection, List<Language> languages) throws JSONException{
		
		JSONObject jObject = new JSONObject();
		jObject.put(LAST_CONNECTION, lastConnection);
		JSONLanguages jLanguages = new JSONLanguages(languages);
		jObject.put(LANGUAGES, jLanguages.toJSON());
		
		return jObject.toString();
	
	}
}
