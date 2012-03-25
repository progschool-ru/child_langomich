package org.omich.lang.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.omich.lang.words.Language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LanguagesData {
	
	private String[] allLanguageColoms = { MySQLiteHelper.LANGUAGE_ID, MySQLiteHelper.LANGUAGE_TEXT_ID, MySQLiteHelper.NAME };
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private WordsData wordsData; 
	
	public LanguagesData(Context context) {
		dbHelper = new MySQLiteHelper(context);
	
	}
	
	public void open(){
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		database.close();
	}
	
	public long createLanguage(Language language){
		
		ContentValues value = new ContentValues();
		
		value.put(MySQLiteHelper.LANGUAGE_TEXT_ID, language.getServerId());
		value.put(MySQLiteHelper.NAME, language.getName());
		
		long languageId = database.insert(MySQLiteHelper.LANGUAGES_TABLE, null, value);
		
		Log.d("words","create words for lang " + languageId);
		
		wordsData.setLanguage(languageId);
		wordsData.createWords(language.getWords());
		
		return languageId; 
	}
	
	public void createLanguages(List<Language> languages){
		wordsData = new WordsData(database);
		ListIterator<Language> iter = languages.listIterator();
		
		while(iter.hasNext()){
			createLanguage(iter.next());
		}
		
	}
	
	public List<Language> getListAllLanguages(){
		
		List<Language> languages = new ArrayList<Language>();
		
		Cursor cursor = getCursorAllLanguage();
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Language language = cusorToLanguage(cursor);
			languages.add(language);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return languages;
	}
	
	public boolean isEmpty(){
		
		Cursor cursor = getCursorAllLanguage();
	
		boolean empty = !cursor.moveToFirst();
	
		cursor.close();
		
		return empty;
	}
	/*
	public void deleteLanguage(Language language){
		String language_id = language.getId();
		
		database.delete(MySQLiteHelper.LANGUAGES_TABLE,
				MySQLiteHelper.LANGUAGE_ID + " = " + language_id, null);
	}
	
	*/
	
	private Cursor getCursorAllLanguage(){
		Cursor cursor = database.query(MySQLiteHelper.LANGUAGES_TABLE, 
				allLanguageColoms, null, null, null, null, null);
		
		return cursor;
	}
	
	private Language cusorToLanguage(Cursor cursor){
		int id = cursor.getInt(0);
	//	String languageId = cursor.getString(1);
		String name = cursor.getString(2);
		
		return new Language(name, id);
	}
	
}
