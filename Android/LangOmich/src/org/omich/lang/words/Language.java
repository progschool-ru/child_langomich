package org.omich.lang.words;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

public class Language implements ILanguage{
	
	private String name;
	private String server_id;
	private int id;
	
	private List<IWord> words;
	
	public Language(int id, String name, String server_id, List<IWord> words){
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
	public String getServerId(){
		return server_id;
	}
	
	public void setServerId(String id){
		this.server_id = id;
	}
	
	public List<IWord> getWords(){
		return words;
	}
	
	public void setWords(List<IWord> words ){
		this.words = words;
	}
	
	@Override
	public String toString(){
		return name;
	}

	public List<IWord> getWors() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentValues toContentValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//тут надо выбрасить свое исключение
	private void parseWords(JSONArray jWords) throws JSONException{
		
		words = new ArrayList<IWord>();
		
		for (int i=0; i<jWords.length();i++){
			IWord word= new Word(jWords.getJSONObject(i));
			words.add(word);
		}
	}
}
