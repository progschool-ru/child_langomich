package org.omich.lang.app.db;

import java.util.ArrayList;
import java.util.List;

public class Db
{
	private List<Word> mWords = new ArrayList<Word>();

	void addWord (String nativ, String foreign, long languageId)
	{
		mWords.add(new Word(nativ, foreign, 0));
	}
	
	List<Word> getWords ()
	{
		List<Word> answer = new ArrayList<Word>();
		
		for(Word w : mWords)
		{
			Word word = new Word(w.nativ, w.foreign, w.rating);
			answer.add(word);
		}
		
		return answer;
	}
}
