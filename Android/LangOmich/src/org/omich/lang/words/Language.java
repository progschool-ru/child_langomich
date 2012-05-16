package org.omich.lang.words;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Language{
	
	public static final String LANGUAGE_ID = "language_id";
	public static final String NAME = "name";
	public static final String LANGUAGE_SERVER_ID = "id";
	public static final String WORDS = "words";
	
	private String name;
	private String server_id;
	private int id;
	
	private List<Word> words;
	
	public Language(int id, String server_id,  String name, List<Word> words){
		this.id = id;
		this.name = name;
		this.server_id = server_id;
		this.words = words;
	}
	
	public Language(JSONObject jLanguage) throws JSONException{
		
		this.id = -1;
		this.server_id = jLanguage.getString(LANGUAGE_SERVER_ID);
		this.name =jLanguage.getString(name);
		parseWords(jLanguage.getJSONArray(WORDS));
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getServerId(){
		return server_id;
	}
	
	public void setServerId(String id){
		this.server_id = id;
	}
	
	public List<Word> getWords(){
		return words;
	}
	
	public void setWords(List<Word> words ){
		this.words = words;
	}
	
	@Override
	public String toString(){
		return name;
	}

	public List<Word> getWors() {
		return words;
	}

	public JSONObject toJSON() throws JSONException {
		
		JSONObject jLanguage = new JSONObject();
		jLanguage.put(LANGUAGE_SERVER_ID, server_id);
		jLanguage.put(NAME, name);
		
		JSONArray jWords = wordsToJson();
		jLanguage.put(WORDS, jWords);
		
		return jLanguage;
	}
	
	//тут надо выбрасить свое исключение
	private void parseWords(JSONArray jWords) throws JSONException{
		
		words = new ArrayList<Word>();
		
		for (int i=0; i<jWords.length();i++){
			Word word= new Word(jWords.getJSONObject(i));
			words.add(word);
		}
	}
	
	private JSONArray wordsToJson() throws JSONException{
		JSONArray jWords = new JSONArray();
		
		if(words != null){
			ListIterator<Word> iter = words.listIterator();
		
			while(iter.hasNext()){
				Word current = iter.next();
				jWords.put(current.toJSON());
			}
		}
		
		return jWords;
		
	}

}
