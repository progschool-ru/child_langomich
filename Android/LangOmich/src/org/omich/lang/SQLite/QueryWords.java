package org.omich.lang.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.omich.lang.words.Word;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryWords implements IQueryWords{

	private String[] columns = { MySQLiteHelper.WORD_ID, MySQLiteHelper.ORIGINAL, MySQLiteHelper.TRANSLATION, MySQLiteHelper.RATING, 
			MySQLiteHelper.MODIFIED, MySQLiteHelper.WORD_IN_SERVER };
	
	private SQLiteDatabase database;
	private int languageId; 
	
	public QueryWords(SQLiteDatabase database, int languageId){
		this.database = database;
		this.languageId = languageId;
	}
	
	public List<Word> getWords() {
		
		List<Word> words = new ArrayList<Word>(); 
		
		Cursor cursor = database.query(MySQLiteHelper.WORDS_TABLE, columns, 
				MySQLiteHelper.WORDS_LANGUAGE+" = "+languageId+" and "+MySQLiteHelper.TRANSLATION+" not null", null, 
				null, null, null);
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Word word = CursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		
		return words;
	}

	public List<Word> getLatestUserWords(long lastSySnc) {
		
		List<Word> words = new ArrayList<Word>();
		
		Cursor cursor = database.query(MySQLiteHelper.WORDS_TABLE, columns, 
				MySQLiteHelper.WORDS_LANGUAGE+" = "+languageId+" and "+MySQLiteHelper.MODIFIED +" >= "+lastSySnc, 
				null, null, null, null);
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Word word = CursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		
		return words;
	}
	
	public Word findWord(Word word, int findMode) {
		
		Cursor cursor;
		
		Word result = null;
		switch(findMode){
			case  FIND_BY_ID:
				cursor = database.query(MySQLiteHelper.WORDS_TABLE, columns, MySQLiteHelper.WORD_ID+" = "+word.getId(), null, null, null, null);
				if(cursor.moveToFirst()){
					 result = CursorToWord(cursor);
				}
				break;
			case  FIND_BY_ORIGINAL:
				String[] original = { word.getOriginal() }; 
				cursor = database.rawQuery("SELECT * FROM "+MySQLiteHelper.WORDS_TABLE+" WHERE "
						+MySQLiteHelper.WORDS_LANGUAGE+" = "+languageId+" and "+MySQLiteHelper.ORIGINAL+" =? and "+MySQLiteHelper.TRANSLATION+" not null", original);
				
				if(cursor.moveToFirst()){
					result = CursorToWord(cursor);
				}
				break;
		}
		
		
		return result;
	}

	public long createWord(Word word) {
		
		if(findWord(word, IQueryWords.FIND_BY_ORIGINAL) != null){
			return -1;
		}
		ContentValues value = wordToContentValue(word);
		return database.insert(MySQLiteHelper.WORDS_TABLE, null, value);
	}

	public long updateWord(Word word, int updateMode) {
		
		ContentValues value  =  wordToContentValue(word);
		
		long res = 0;
		
		switch(updateMode){
			case UPDATE_BY_ID:
				database.update(MySQLiteHelper.WORDS_TABLE, value, MySQLiteHelper.WORD_ID+" = "+word.getId(), null);
				break;
			case UPDATE_BY_ORIGINAL:
				deleteWord(word);
				res = createWord(word);
				break;
		}
		
		return res;
	}
	
	public void createWords(List<Word> words) {
		ListIterator<Word> iter = words.listIterator();
		
		while(iter.hasNext()){
			createWord(iter.next());
		}
	}
	
	public void deleteWord(Word word) {
		if(word.getInServer()){
			toDel(word);
		}else{
			delWord(word);
		}
	}
	
	private void toDel(Word word){
		word.setTranslation(null);
		updateWord(word, UPDATE_BY_ID);
	}
	
	private void delWord(Word word){
		database.delete(MySQLiteHelper.WORDS_TABLE, MySQLiteHelper.WORD_ID+" = "+word.getId(), null);
	}
	
	private Word CursorToWord(Cursor cursor){
		
		long id = cursor.getLong(0);
		String original  = cursor.getString(1);
		String translation = cursor.getString(2);
		if(translation == null) translation = "";
		int rating = cursor.getInt(3);
		long modified = cursor.getLong(4);
		int in_server = cursor.getInt(5);
		
		return new Word(id, original, translation, rating, modified, in_server);
	}
	
	private ContentValues wordToContentValue (Word word){
		
		ContentValues value = new ContentValues();
		
		value.put(MySQLiteHelper.ORIGINAL, word.getOriginal());
		value.put(MySQLiteHelper.TRANSLATION, word.getTranslation());
		value.put(MySQLiteHelper.WORDS_LANGUAGE, languageId);
		value.put(MySQLiteHelper.RATING, word.getRating());
		value.put(MySQLiteHelper.MODIFIED, word.getModified());
		value.put(MySQLiteHelper.WORD_IN_SERVER, word.getInServer());
		
		return value;
	}

	public void deleteWords(List<Word> words) {
		ListIterator<Word> iter = words.listIterator();
		
		while(iter.hasNext()){
			deleteWord(iter.next());
		}
	}

	public void updateWords(List<Word> words) {
	
		ListIterator<Word> iter = words.listIterator();
		
		while(iter.hasNext()){
			
			Word currentWord = iter.next();
			Word in_base = findWord(currentWord, FIND_BY_ID);
			
			if(in_base == null){
				createWord(currentWord);
			}else{
				String translation = currentWord.getTranslation();
				
				if(translation.isEmpty()){
					delWord(in_base);
				}else{
					long id = in_base.getId();
					currentWord.setId(id);
					updateWord(currentWord, UPDATE_BY_ID);
				}
			}
		}
	}

	
	
	
}