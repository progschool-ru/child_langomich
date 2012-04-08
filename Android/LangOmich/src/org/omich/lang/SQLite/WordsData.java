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
	
	public WordsData(Context context){
		dbHelper = new MySQLiteHelper(context);
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
		//TODO переименовать метод в sync
		ListIterator<Word> iter = words.listIterator();
		
		while(iter.hasNext()){
			Word word = iter.next();
			String translation = word.getTranslation();
			if(!translation.equals("")){
				createWord(word);
			}else{
				//TODO: сделать после того как подчиню апишку на сервере
			}
		}
		
	}
	
	public long createWord(Word word){
		
		ContentValues value = wordToContentValues(word);
		return database.insert(MySQLiteHelper.WORDS_TABLE, null, value);
		
	}
	
	public List<Word> getAllWords(){
		
		List<Word> words = new ArrayList<Word>();
		
		//тут надо добавить условие перевод != пустой строке
		Cursor cursor = database.query(MySQLiteHelper.WORDS_TABLE,
						wordColoms, MySQLiteHelper.WORDS_LANGUAGE + " = " + languageId +
						" and " + MySQLiteHelper.TRANSLATION + " not null  ",
						null, null, null, null);
		
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Word word = cursortoWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		cursor.close();
		
		return words;
	}
	
	public void update(Word word){
		
		ContentValues value = wordToContentValues(word);
		database.update(MySQLiteHelper.WORDS_TABLE, value, MySQLiteHelper.WORD_ID+"="+word.getId(), null);
		
	}
	
	public void toDel(Word word){
		word.setTranslation(null);
		update(word);
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
	
	private ContentValues wordToContentValues(Word word){
		
		ContentValues value = new ContentValues();
		value.put(MySQLiteHelper.ORIGINAL, word.getOriginal());
		value.put(MySQLiteHelper.TRANSLATION, word.getTranslation());
		value.put(MySQLiteHelper.WORDS_LANGUAGE, languageId);
		value.put(MySQLiteHelper.RATING, word.getRating());
		value.put(MySQLiteHelper.MODIFIED,word.getModified());
		
		return value;
	}
}
