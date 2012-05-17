package org.omich.lang.SQLite;

import java.util.List;

import org.omich.lang.words.Word;

public interface IQueryWords {
	

	public final int FIND_BY_ID = 0;
	public final int FIND_BY_ORIGINAL = 1;
	
	public final int UPDATE_BY_ID = 0;
	public final int UPDATE_BY_ORIGINAL = 1;
	
	public List<Word> getWords();
	public Word findWord(Word word, int findMode);
	public long createWord(Word word);
	public void createWords(List<Word> words);
	public long updateWord(Word word, int updateMode);
	public void updateWords(List<Word> words);
	public void deleteWord(Word word);
	public void deleteWords(List<Word> word);
}
