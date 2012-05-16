package org.omich.lang.SQLite;

import org.omich.lang.words.Language;

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

}
