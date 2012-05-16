package org.omich.lang.SQLite;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.words.Language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordsStorage implements IWordsStorage{
	
	public static final int FIND_BY_ID = 0;
	public static final int FIND_BY_SERVER_ID = 1;
	public static final int FIND_BY_NAME = 2;
	
	
	SQLiteDatabase wordsStorage;
	MySQLiteHelper myHelper;
	
	String[] langugeColoms = { MySQLiteHelper.LANGUAGE_ID, MySQLiteHelper.LANGUAGE_SERVER_ID, MySQLiteHelper.NAME};
	
	public WordsStorage(Context context){
		myHelper = new MySQLiteHelper(context);
	}
	
	public void open(){
		wordsStorage = myHelper.getWritableDatabase();
	}
	
	public void close(){
		wordsStorage.close();
	}
	
	public List<Language> getLatestUserWords(long lastSySnc) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Language> getLanguages() {
		
		List<Language> languages = new ArrayList<Language>();
		
		Cursor cursor = wordsStorage.query(MySQLiteHelper.LANGUAGES_TABLE, langugeColoms, MySQLiteHelper.NAME+" not null", null, null, null, null);
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Language language = cursorToLanguage(cursor);
			languages.add(language);
			cursor.moveToNext();
		}
		
		cursor.close();
		return languages;
	}

	public IQueryLanguage getQueryLanguage(int languageId) {
		
		Language language = new Language(languageId, null, null, null);
		
		language = findLanguage(language, FIND_BY_ID);
		if (language != null){
			return new QueryLanguage(wordsStorage, language);
		}else{
			return null;
		}
	}

	private Language cursorToLanguage(Cursor cursor){
		
		int id = cursor.getInt(0);
		String server_id = cursor.getString(1);
		String name = cursor.getString(2);
		
		return new Language(id, server_id, name, null);
	}

	public void createOrUpdate(List<Language> languages) {
		
	}
	
	private Language findLanguage(Language language, int find){
		
		Cursor cursor = null;
		
		switch(find){
			case FIND_BY_ID:
				cursor = wordsStorage.query(MySQLiteHelper.LANGUAGES_TABLE, langugeColoms,
						MySQLiteHelper.LANGUAGE_ID+" = "+language.getId(), null, null, null, null);
				break;
			case FIND_BY_SERVER_ID:
				String[] serverId = { language.getServerId() };
				cursor = wordsStorage.query(MySQLiteHelper.LANGUAGES_TABLE, langugeColoms,
						MySQLiteHelper.LANGUAGE_SERVER_ID+" = ?", serverId, null, null, null);
				break;
			case FIND_BY_NAME:
				String[] name = { language.getName() };
				cursor = wordsStorage.query(MySQLiteHelper.LANGUAGES_TABLE, langugeColoms,
						MySQLiteHelper.NAME+" = ?", name, null, null, null);
				break;
		}
		
		if(cursor.moveToFirst()){
			return cursorToLanguage(cursor);
		}
		
		return null;
	}

	public long create(Language language) {
		
		Language in_db = findLanguage(language, 2); 
		
		if(in_db == null){ 
			ContentValues contentValue = languagetoContentValue(language);
			 return wordsStorage.insert(MySQLiteHelper.LANGUAGES_TABLE, null, contentValue);
			
		}
		
		return -1;
	}

	public void synchronization(List<Language> languages) {
		// TODO Auto-generated method stub
		
	}
	
	private ContentValues languagetoContentValue(Language language){
		
		ContentValues contentValues = new ContentValues();
		
		if(language.getServerId() == null){
			contentValues.put(MySQLiteHelper.LANGUAGE_SERVER_ID, "");
		}else{
			contentValues.put(MySQLiteHelper.LANGUAGE_SERVER_ID, language.getServerId());
		}
		
		contentValues.put(MySQLiteHelper.NAME, language.getName());
		
		return contentValues;
	}
}
