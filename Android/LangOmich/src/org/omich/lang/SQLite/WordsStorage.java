package org.omich.lang.SQLite;

import java.util.List;

import org.omich.lang.words.IWord;
import org.omich.lang.words.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordsStorage implements IWordsStorage{

	MySQLiteHelper myHelper;
	SQLiteDatabase wordsStorage;
	
	private String[] WordColumn = {IWord.WORD_ID, IWord.ORIGINAL, IWord.TRANSLATION, IWord.RATING, IWord.MODIFIED, IWord.WORD_IN_SERVER};
	
	int language_id;
	
	public WordsStorage(Context context, int language_id){
		myHelper = new MySQLiteHelper(context);
		this.language_id = language_id;
	}
	
	public WordsStorage(SQLiteDatabase wordsStorage, int language_id){
		this.wordsStorage = wordsStorage;
	}
	
	public void open(){
		wordsStorage = myHelper.getWritableDatabase();
	}
	
	public void close(){
		wordsStorage.close();
	}
	
	public List<IWord> getWords() {
		return null;
	}

	public List<IWord> getLatestUserWords(long lastSync) {
		return null;
	}

	public boolean setWords(List<IWord> words) {
		return false;
	}
	
	public void createOrUpdate(IWord word){
		
	
	}
	
	private long createWord(IWord word){
		ContentValues values = word.toContentValues(language_id);
		return wordsStorage.insert(MySQLiteHelper.WORDS_TABLE, null, values);
	}
	
	private void updateWord(IWord word){
		ContentValues values = word.toContentValues(language_id);
		wordsStorage.update(MySQLiteHelper.WORDS_TABLE, values, IWord.LANGUAGE_ID+" = "+word.getId(), null);
	}
	
	private IWord cursorToWord(Cursor cursor){
	
		long id = cursor.getLong(0);
		String original = cursor.getString(1);
		String translation = cursor.getString(2);
		int rating = cursor.getInt(3);
		long modified = cursor.getLong(4);
		int in_server = cursor.getInt(5);
		
		return new Word(id, original, translation, rating, modified, in_server);
	}
}
