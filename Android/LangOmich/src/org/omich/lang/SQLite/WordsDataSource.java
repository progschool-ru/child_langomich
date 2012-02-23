package org.omich.lang.SQLite;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.words.Language;
import org.omich.lang.words.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	private String[] allLanguageColoms = { MySQLiteHelper.LANGUAGE_ID, MySQLiteHelper.NAME };
	
	public WordsDataSource(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void createLanguage(Language language){
		ContentValues value = new ContentValues();
		
		value.put(MySQLiteHelper.LANGUAGE_ID, language.getId());
		value.put(MySQLiteHelper.NAME, language.getName());

		database.insert(MySQLiteHelper.LANGUAGES_TABLE, null, value);
	}
	
	public List<Language> getAllLanguages(){
		
		List<Language> languages = new ArrayList<Language>();
		
		Cursor cursor = database.query(MySQLiteHelper.LANGUAGES_TABLE, 
				allLanguageColoms, null, null, null, null, null);
		
		cursor.moveToFirst();
		
		while(cursor.isAfterLast()){
			Language language = cusorToLanguage(cursor);
			languages.add(language);
			cursor.moveToNext();
		}
		
		return languages;
	}
	
	public void deleteLanguage(Language language){
		String language_id = language.getId();
		
		database.delete(MySQLiteHelper.LANGUAGES_TABLE,
				MySQLiteHelper.LANGUAGE_ID + " = " + language_id, null);
	}
	
	private Language cusorToLanguage(Cursor cursor){
		
		String languageId = cursor.getString(0);
		String name = cursor.getString(1);
		
		return new Language(name, languageId, null);
	}
	
}
