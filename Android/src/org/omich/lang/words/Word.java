package org.omich.lang.words;

import org.json.JSONException;
import org.json.JSONObject;

public class Word {
	
	private static final String ORIGINAL_STR = "original";
	private static final String TRANSLATION_STR = "translation";
	
	private String original;
	private String translation;
	private String languageName;
	
	public Word(JSONObject jObject) throws JSONException{
		original = jObject.getString(ORIGINAL_STR);
		translation = jObject.getString(TRANSLATION_STR);
	}
	
	public void setLanguages(String langName){
		languageName = langName;
	}
	
	public String getOriginal(){
		return original;
	}
	
	public String getTranslation(){
		return translation;
	}
	
	public String getLanguageName(){
		return languageName;
	}
}
