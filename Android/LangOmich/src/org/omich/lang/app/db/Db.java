package org.omich.lang.app.db;

import java.util.ArrayList;
import java.util.List;

public class Db
{
	private List<Word> mWords = new ArrayList<Word>();
	
	public Db ()
	{
		mWords.add(new Word("первый", "first", 0));
		mWords.add(new Word("второй", "second", 0));
	}

	public void addWord (String nativ, String foreign, long languageId)
	{
		mWords.add(new Word(nativ, foreign, 0));
	}
	
	public List<Word> getWords ()
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
