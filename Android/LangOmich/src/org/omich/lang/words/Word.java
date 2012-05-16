package org.omich.lang.words;

import org.json.JSONException;
import org.json.JSONObject;

public class Word {
	
	
	private static final String ORIGINAL = "original";
	private static final String TRANSLATION = "translation";
	private static final String RATING = "rating";
	private static final String MODIFIED = "modified";
	
	public static final int WORD_IS_NOT_LOAD_TO_SERVER = 0;
	public static final int WORD_IS_LOAD_TO_SERVER = 1;
	
	private long id;
	private String original;
	private String translation;
	private int rating;
	private long modified;
	private boolean in_server;
	
	
	//Нужен ли вобще ?
	public Word(String original, String translation, int rating, long modified){
		
		this.rating = rating;
		this.original = original;
		this.translation = translation;
		this.modified = modified;
		this.in_server = true;
	}
	
	//Конструктор вызывается при парсинге JSON, тут надо выбрасит свое исключение
	public Word(JSONObject jWord) throws JSONException{
		
		this.id = -1;
		this.original = jWord.getString(ORIGINAL);
		this.translation = jWord.getString(TRANSLATION);
		this.rating = jWord.getInt(RATING);
		if(jWord.has(MODIFIED)){
			this.modified = jWord.getLong(MODIFIED);
		}else{
			this.modified = 0;
		}
		this.in_server = true;
	}
	
	//Конструктор вызывается при выборе слова с базы данных или создании нового 
	public Word(long id, String original, String translation, int rating, long modified, int in_server){
		
		this.id = id;
		this.rating = rating;
		this.original = original;
		this.translation = translation;
		this.modified = modified;
		this.in_server = intToBoolean(in_server);
		
	}
	
	public boolean getInServer(){
		return in_server;
	}
	
	public void setInServer(int in_server){
		this.in_server = intToBoolean(in_server); 
	}
	
	private boolean intToBoolean(int myBoolena){
		
		boolean result;
		
			switch(myBoolena){
				case WORD_IS_LOAD_TO_SERVER:
					result = true;
					break;
				case WORD_IS_NOT_LOAD_TO_SERVER:
					result = false;
					break;
				default:
					result = false;
					break;
			}
		
			return result;
	}
	
	public String getOriginal(){
		return original;
	}
	
	public void setOriginal(String original){
		this.original = original;
	}
	
	public String getTranslation(){
		return translation;
	}
	
	public void setTranslation(String translation){
		this.translation = translation;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public int getRating(){
		return rating;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public long getModified(){
		return modified;
	}
	
	public void setModified(long modified){
		this.modified = modified;
	}
	
	//тут надо выбросить свое исключение
	public JSONObject toJSON() throws JSONException{
		
		JSONObject jWord = new JSONObject();
		jWord.put(ORIGINAL, original);
		jWord.put(TRANSLATION, translation);
		jWord.put(RATING, rating);
		jWord.put(MODIFIED, modified);
		
		return jWord;
	}
	
}
