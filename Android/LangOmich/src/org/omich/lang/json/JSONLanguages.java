package org.omich.lang.json;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.omich.lang.words.Language;
import org.omich.lang.words.Word;


public class JSONLanguages {

	private static final String NAME = "name";
	private static final String ID = "id";
	private static final String WORDS = "words";
	
	
	public JSONArray toJSON(List<Language> languages) throws JSONException{
		
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
		
		languages.put(ID, lang.getId());
		languages.put(NAME, lang.getName());
		
		JSONWords word = new JSONWords(lang.getWords());
		languages.put(WORDS,word.toJSON() );
		
		return languages;
	}
	
	public static List<Language> parse(JSONArray jArray) throws JSONException{
		
		List<Language> languages = new ArrayList<Language>();
		
		for(int i=0; i<jArray.length(); i++){
			
			Language language = parse(jArray.getJSONObject(i));
			languages.add(language);
		}
		
		return languages;
	}
	
	private static Language parse(JSONObject jObject) throws JSONException {
		
		String name = jObject.getString(NAME);
		String id = jObject.getString(ID);
		List<Word> words = JSONWords.Parse(jObject.getJSONArray(WORDS));
		
		Language language = new Language(name, id, words);
		
		return language;
	}
	
	
}
