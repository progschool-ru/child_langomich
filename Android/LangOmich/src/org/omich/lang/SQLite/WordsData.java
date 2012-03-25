package org.omich.lang.SQLite;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.omich.lang.words.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WordsData {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	private long languageId;
	
	private String[] wordColoms = {MySQLiteHelper.WORD_ID, MySQLiteHelper.ORIGINAL, MySQLiteHelper.TRANSLATION, MySQLiteHelper.RATING, MySQLiteHelper.MODIFIED };
	
	public WordsData(Context context, long languageId){
		dbHelper = new MySQLiteHelper(context);
		this.languageId = languageId;
	}
	
	public WordsData(SQLiteDatabase data){
		database = data;
	}
	
	public void setLanguage(long languageId){
		this.languageId = languageId;
	}
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void createWords(List<Word> words){
		
		ListIterator<Word> iter = words.listIterator();
		
		while(iter.hasNext()){
			createWord(iter.next());
		}
		
	}
	
	public long createWord(Word word){
		
		ContentValues value = new ContentValues();
		value.put(MySQLiteHelper.ORIGINAL, word.getOriginal());
		value.put(MySQLiteHelper.TRANSLATION, word.getTranslation());
		value.put(MySQLiteHelper.WORDS_LANGUAGE, languageId);
		value.put(MySQLiteHelper.RATING, word.getRating());
		value.put(MySQLiteHelper.MODIFIED,word.getModified());
		
		Log.d("words", word.getOriginal()+ " "+word.getTranslation()+" "+Integer.toString(word.getRating())+" "+Long.toString(word.getModified())+" "+languageId);
		long id = database.insert(MySQLiteHelper.WORDS_TABLE, null, value);
		Log.d("words", "words_id="+id);
		return id;
	}
	
	public List<Word> getAllWords(){
		
		List<Word> words = new ArrayList<Word>();
		
		
		Cursor cursor = database.query(MySQLiteHelper.WORDS_TABLE,
						wordColoms, MySQLiteHelper.WORDS_LANGUAGE + " = " + languageId,
						null, null, null, null);
		
		
		cursor.moveToFirst();
		
		int index = 0;
		
		while(!cursor.isAfterLast()){
			Log.d("words", Integer.toString(index++) );
			Word word = cursortoWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		cursor.close();
		
		return words;
	}
	
	public void deleteWord(Word word){
		
		database.delete(MySQLiteHelper.WORDS_TABLE,
				MySQLiteHelper.WORD_ID+"="+word.getId(), null);
	}
	
	private Word cursortoWord(Cursor cursor){
		long id = cursor.getLong(0);
		String original = cursor.getString(1);
		String translation = cursor.getString(2);
		int rating = cursor.getInt(3);
		long modified = cursor.getLong(4);
		
		return new Word(id, original, translation, rating, modified);
	}
	
}
