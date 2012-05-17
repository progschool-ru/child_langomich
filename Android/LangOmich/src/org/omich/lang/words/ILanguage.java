package org.omich.lang.words;

import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;

public interface ILanguage {
	
	public static final String LANGUAGE_ID = "language_id";
	public static final String NAME = "name";
	public static final String LANGUAGE_SERVER_ID = "id";
	public static final String WORDS = "words";
	
	public List<IWord> getWords();
	public int getId();
	public String getName();
	public String getServerId();
	
	public ContentValues toContentValues();
	public JSONObject toJSON();
}
