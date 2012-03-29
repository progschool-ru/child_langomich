package org.omich.lang.json;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.omich.lang.words.Word;

public class JSONWords {
	
	private static final String ORIGINAL = "original";
	private static final String TRANSLATION = "translation";
	private static final String RATING = "rating";
	private static final String MODIFIED = "modified";
	
	private List<Word> words;
	
	public JSONWords(List<Word> words){
		this.words = words;
	}
	
	public JSONWords(){
		words = new ArrayList<Word>();
	}
	
	public void put(Word word){
		words.add(word);
	}
	
	public JSONArray toJSON() throws JSONException{
		
		JSONArray jWords = new JSONArray();
		
		ListIterator<Word> iter = words.listIterator();
		
		while(iter.hasNext()){
			
			JSONObject jObject = wordToJSON(iter.next());
			jWords.put(jObject);
		}
		
		return jWords;
	}
	
	public static JSONObject wordToJSON(Word word) throws JSONException{
		
		JSONObject jObject = new JSONObject();
		jObject.put(ORIGINAL, word.getOriginal());
		jObject.put(TRANSLATION, word.getTranslation());
		jObject.put(RATING, word.getRating());
		jObject.put(MODIFIED, word.getModified());
		
		return jObject;
	}
	
	@Override
	public String toString(){
		
		String str;
		try{
			str =  toJSON().toString();
		}catch(JSONException e){
			str = e.getMessage();
		}
		return str;
	}
	
	public static List<Word> parse(JSONArray jWords) throws JSONException{
		
		List<Word> words = new ArrayList<Word>();
		
		for(int i = 0; i<jWords.length(); i++){
			Word word = parseWord(jWords.getJSONObject(i));
			words.add(word);
		}
		
		return words;
	}
	
	
	public static Word parseWord(JSONObject jObject) throws JSONException{
		
		String original = jObject.getString(ORIGINAL);
		String translation = jObject.getString(TRANSLATION);
		int rating = (int) jObject.getLong(RATING);
		long modified;
		
		try{
		
			modified  =  Long.parseLong(jObject.getString(MODIFIED));
		}catch(JSONException e){
			modified = 0;
		}
		
		//Log.d("word", original);
		Word word = new Word(original, translation, rating, modified);
		
		return word;
	}
	
}
