package org.omich.lang.SQLite;

import org.omich.lang.words.Language;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class QueryLanguage implements IQueryLanguage {
	
	Language language;
	SQLiteDatabase database;
	
	public QueryLanguage(SQLiteDatabase database, Language language){
		this.language = language;
		this.database = database;
	}
	
	public Language getLanguage() {
		return language;
	}

	public IQueryWords getQueryWords() {
		return new QueryWords(database, language.getId());
	}
	
	public void updateServerId(String serverId){
		language.setServerId(serverId);
		ContentValues value = languagetoContentValue(language);
		database.update(MySQLiteHelper.LANGUAGES_TABLE, value, MySQLiteHelper.LANGUAGE_ID+" = "+language.getId(), null);
	}
	
	public void updateName(String name){
		language.setName(name);
		ContentValues value = languagetoContentValue(language);
		database.update(MySQLiteHelper.LANGUAGES_TABLE, value, MySQLiteHelper.LANGUAGE_ID+" = "+language.getId(), null);
	}
	
	public void delete(){
		database.delete(MySQLiteHelper.LANGUAGES_TABLE, MySQLiteHelper.LANGUAGE_ID+" = "+language.getId(), null);
		language.setId(0);
	}
	
	private ContentValues languagetoContentValue(Language language){
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteHelper.LANGUAGE_SERVER_ID, language.getServerId());
		contentValues.put(MySQLiteHelper.NAME, language.getName());
		
		return contentValues;
	}
}
