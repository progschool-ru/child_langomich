package org.omich.lang.words;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

public interface IWord {
	
	public static final String WORD_ID = "word_id";
	public static final String ORIGINAL = "original";
	public static final String TRANSLATION = "translation";
	public static final String RATING = "rating";
	public static final String MODIFIED = "modified";
	public static final String LANGUAGE_ID = "languages_id";
	public static final String WORD_IN_SERVER = "words_in_server";
	
	public static final int WORD_IS_NOT_LOAD_TO_SERVER = 0;
	public static final int WORD_IS_LOAD_TO_SERVER = 1;
	
	long getId();
	String getOriginal();
	String getTranslation();
	int getRating();
	long getModified();
	boolean getInServer();
	
	public ContentValues toContentValues(int language_id);
	public JSONObject toJSON() throws JSONException;
}