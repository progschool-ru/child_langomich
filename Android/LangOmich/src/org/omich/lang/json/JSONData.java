package org.omich.lang.json;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.words.Language;

public class JSONData {
	
	private static final String LANGUAGES = "languages";
	private static final String SUCCESS = "success";
	private static final String LAST_CONNECTION = "lastConnection";
	
	private long lastconnect = 0;
	private boolean sucsess;
	private List<Language> languages = new ArrayList<Language>();
	
	public JSONData(){
		sucsess = false;
	}
	
	
	public JSONData(String jString) throws JSONException{
		
		JSONObject jObject = new JSONObject(jString);
		
		sucsess = jObject.getBoolean(SUCCESS);
		
		if(sucsess){
		
			if(jObject.has(LAST_CONNECTION)){
				lastconnect = jObject.getLong(LAST_CONNECTION);
			}
		
			JSONArray jLanguages = jObject.getJSONArray(LANGUAGES);
			
			for(int i=0; i<jLanguages.length(); i++){
				Language languge = new Language(jLanguages.getJSONObject(i));
				languages.add(languge);
			}
		}
	}
	
	public long getLastConnect(){
		if(sucsess){
			return lastconnect;
		}
		return 0;
	}
	
	public List<Language> getLanguage(){
		if(sucsess){
			return languages;
		}
		return null;
	}
	
	public void put(List<Language> languages, long lastconnect){
		this.languages = languages;
		this.lastconnect = lastconnect;
	}
	
	@Override
	public String toString(){
		
		JSONObject jObject = new JSONObject();
		
		JSONArray jLanguage = new JSONArray();
		
		ListIterator<Language> iter = languages.listIterator();
		
		
		try {
			
			while(iter.hasNext()){
				jLanguage.put(iter.next().toJSON());
			}
		
			jObject.put(LANGUAGES, jLanguage);
			jObject.put(LAST_CONNECTION, lastconnect);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		
		return jObject.toString();
	}
}
