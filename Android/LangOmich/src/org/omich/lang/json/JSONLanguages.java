package org.omich.lang.json;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.words.Language;
import org.omich.lang.words.Word;


public class JSONLanguages {

	private static final String NAME = "name";
	private static final String ID = "id";
	private static final String WORDS = "words";
	
	private List<Language> languages;
	
	public JSONLanguages(){
		languages = new ArrayList<Language>();
	}
	
	public JSONLanguages(List<Language> languages){
		this.languages = languages;
	}
	
	public void put(Language language){
		languages.add(language);
	}
	
	public JSONArray toJSON() throws JSONException{
		
		JSONArray jArray = new JSONArray();
		
		ListIterator<Language> iter = languages.listIterator();
		
		while(iter.hasNext()){
			
			JSONObject jObject = langToJSON(iter.next());
			jArray.put(jObject);
		}
		
		return jArray;
	}
	
	private JSONObject langToJSON(Language lang) throws JSONException{
		
		JSONObject languages = new JSONObject();
		
		languages.put(ID, lang.getServerId());
		languages.put(NAME, lang.getName());
		
		JSONWords word = new JSONWords(lang.getWords());
		languages.put(WORDS,word.toJSON() );
		
		return languages;
	}
	
	@Override
	public String toString(){
		
		String str;
			try{
				str = toJSON().toString();
			}catch(Exception e){
				str = e.getMessage();
			}
		
		return str;
	}
	
	public static List<Language> parse(JSONArray jArray) throws JSONException{
		
		List<Language> languages = new ArrayList<Language>();
		
		for(int i=0; i<jArray.length(); i++){
			
			Language language = parseLanguage(jArray.getJSONObject(i));
			languages.add(language);
		}
		
		return languages;
	}
	
	private static Language parseLanguage(JSONObject jObject) throws JSONException {
		
		String name = jObject.getString(NAME);
		String id = jObject.getString(ID);
		List<Word> words = JSONWords.parse(jObject.getJSONArray(WORDS));
		
		Language language = null; //new Language(name, id, words);
		
		return language;
	}
	
	
}
