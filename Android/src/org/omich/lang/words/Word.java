package org.omich.lang.words;

import org.json.JSONException;
import org.json.JSONObject;

public class Word {
	
	private static final String ORIGINAL_STR = "original";
	private static final String TRANSLATION_STR = "translation";
	
	private long id;
	private String original;
	private String translation;
	private String languageName;
	
	public Word(JSONObject jObject) throws JSONException{
		original = jObject.getString(ORIGINAL_STR);
		translation = jObject.getString(TRANSLATION_STR);
		id = 0;
	}
	
	public Word(){
		original = null;
		translation = null;
		id = 0;
	}
	
	public void setLanguages(String langName){
		languageName = langName;
	}
	
	public void setOriginal(String original){
		this.original = original;
	}
	
	public void setTranslation(String translation){
		this.translation = translation;
	}
	
	public void setId(long id){
		this.id = id;
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
	
	public long getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return original+" "+translation+" "+languageName;
	}
	
}
