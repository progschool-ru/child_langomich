package org.omich.lang.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.omich.lang.words.Language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class LanguagesData {
	
	private String[] allLanguageColoms = { MySQLiteHelper.LANGUAGE_ID, MySQLiteHelper.LANGUAGE_SERVER_ID, MySQLiteHelper.NAME };
	
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
		
		value.put(MySQLiteHelper.LANGUAGE_SERVER_ID, language.getServerId());
		value.put(MySQLiteHelper.NAME, language.getName());
		
		return database.insert(MySQLiteHelper.LANGUAGES_TABLE, null, value); 
	}
	
	public void createLanguages(List<Language> languages){
		//TODO переименовать метод в SyncLang
		wordsData = new WordsData(database);
		ListIterator<Language> iter = languages.listIterator();
		
		while(iter.hasNext()){
		
			Language language = iter.next();
			
			long languageId;
			
			Language find = getLanguage(language.getServerId());
			if(find == null){
				 languageId =  createLanguage(language);
			}else{
				languageId = find.getId();
			}
			
			wordsData.setLanguage(languageId);
			wordsData.createWords(language.getWords());
			
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
	
	public Language getLanguage(String server_id){
		
		String[] param = {server_id};
		Cursor cursor = database.query(MySQLiteHelper.LANGUAGES_TABLE, 
				allLanguageColoms, MySQLiteHelper.LANGUAGE_SERVER_ID +" =  ?",param,  
				null, null, null);
		
		int count = cursor.getCount();
		if(count == 0) return null;
		cursor.moveToFirst();
		
		Language language = cusorToLanguage(cursor);
		cursor.close();
		return language;
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
		String serverId = cursor.getString(1);
		String name = cursor.getString(2);
		return new Language(name, id, serverId);
	}
	
}