package org.omich.lang.words;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Words {
	
	private static final String LANGUAGES = "languages";
	private static final String WORDS = "words";
	private static final String NAME = "name";
	
	Vector<Word> words;
	
	public Words(String jString) throws JSONException{
		
		words = new Vector<Word>();
		
		JSONObject jObject = new JSONObject(jString);
		JSONArray langArray = jObject.getJSONArray(LANGUAGES);
		
		for(int i=0; i<langArray.length(); i++){
			JSONObject lang = langArray.getJSONObject(i);
			String langName = lang.getString(NAME);
			JSONArray wordsArray = lang.getJSONArray(WORDS);
			
			for(int j=0; j<wordsArray.length(); j++){
				JSONObject Jword = wordsArray.getJSONObject(j);
				Word word = new Word(Jword);
				word.setLanguages(langName);
				words.add(word);
			}
			
		}
	}
	
	public int length(){
		return words.size();
	}
	
	public Word getWord(int index){
		return words.get(index);
	}
	
}
