package org.omich.lang.SQLite;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.words.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.WORD_ID, MySQLiteHelper.ORIGINAL, 
								MySQLiteHelper.TRANSLATION, MySQLiteHelper.LANGUAGE};
	
	public WordsDataSource(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Word createWord(Word word){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.ORIGINAL, word.getOriginal());
		values.put(MySQLiteHelper.TRANSLATION, word.getTranslation());
		values.put(MySQLiteHelper.LANGUAGE, word.getLanguageName());
		long insertId = database.insert(MySQLiteHelper.TABLE_WORDS, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS, allColumns, MySQLiteHelper.WORD_ID + "="+ insertId, 
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToWord(cursor);
	}
	
	public void deleteWord(Word word){
		long id = word.getId();
		database.delete(MySQLiteHelper.TABLE_WORDS, MySQLiteHelper.WORD_ID+"="+id, null);
	}
	
	public void deleteAllWords(){
		database.delete(MySQLiteHelper.TABLE_WORDS, null, null);
	}
	
	public List<Word> getAllWords(){
		List<Word> words = new ArrayList<Word>();
		Cursor cursor =  database.query(MySQLiteHelper.TABLE_WORDS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		
		return words;
	}
	
	private Word cursorToWord(Cursor cursor){
		Word word = new Word();
		word.setId(cursor.getLong(0));
		word.setOriginal(cursor.getString(1));
		word.setTranslation(cursor.getString(2));
		word.setLanguages(cursor.getString(3));
		return word;
	}
}
