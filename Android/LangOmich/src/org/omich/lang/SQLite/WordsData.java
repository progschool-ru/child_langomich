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

public class WordsData {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	private Language language;
	
	private String[] wordColoms = {MySQLiteHelper.ORIGINAL, MySQLiteHelper.TRANSLATION, MySQLiteHelper.RATING, MySQLiteHelper.MODIFIED };
	
	public WordsData(Context context, Language language){
		dbHelper = new MySQLiteHelper(context);
		
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void setLanguage(Language language){
		this.language = language;
	}
	
	public void createWord(Word word){
		ContentValues value = new ContentValues();
		value.put(MySQLiteHelper.ORIGINAL, word.getOriginal());
		value.put(MySQLiteHelper.TRANSLATION, word.getTranslation());
		value.put(MySQLiteHelper.LANGUAGE_TEXT_ID, language.getId());
		value.put(MySQLiteHelper.RATING, word.getRating());
		value.put(MySQLiteHelper.MODIFIED,word.getModified());
		
		database.insert(MySQLiteHelper.WORDS_TABLE, null, value);
	}
	
	public List<Word> getAllWords(){
		
		List<Word> words = new ArrayList<Word>();
		
		Cursor cursor = database.query(MySQLiteHelper.WORDS_TABLE,
						wordColoms, MySQLiteHelper.LANGUAGE_TEXT_ID + " = " + language.getId(),
						null, null, null, null);
		
		cursor.moveToFirst();
		
		while(cursor.isAfterLast()){
			Word word = cursortoWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		
		return words;
	}
	
	public void deleteWord(Word word){
		
		database.delete(MySQLiteHelper.WORDS_TABLE,
				MySQLiteHelper.ORIGINAL+"="+word.getOriginal()+" and "+MySQLiteHelper.TRANSLATION +" = "+ word.getTranslation(),
				null);
	}
	
	private Word cursortoWord(Cursor cursor){
		String original = cursor.getString(0);
		String translation = cursor.getString(1);
		int rating = cursor.getInt(2);
		long modified = cursor.getLong(3);
		
		return new Word(original,translation,rating,modified);
	}
	
}
