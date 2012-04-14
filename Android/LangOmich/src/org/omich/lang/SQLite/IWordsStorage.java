package org.omich.lang.SQLite;

import java.util.List;

import org.omich.lang.words.ILanguage;
import org.omich.lang.words.IWord;

public interface IWordsStorage {
	
	public static final String WORDS_TABLE = "words";
	
	
	public static final String CREATE_WORDS_TABLE = "CREATE TABLE " + WORDS_TABLE
			+ "( "
				+ IWord.WORD_ID + " integer primary key autoincrement, "
				+ IWord.ORIGINAL + " text not null, "
				+ IWord.TRANSLATION + " text, "
				+ IWord.LANGUAGE_ID + " integer references "+ILanguageStorage.LANGUAGES_TABLE+"("+ILanguage.LANGUAGE_ID +") on delete cascade, "
				+ IWord.RATING +" integer, "
				+ IWord.MODIFIED + " integer,"
				+ IWord.WORD_IN_SERVER +"integer"
			+")";
	
	public List<IWord> getWords();
	public List<IWord> getLatestUserWords(long lastSySnc);
	public boolean setWords(List<IWord> words);
	public void open();
	public void close();
}
