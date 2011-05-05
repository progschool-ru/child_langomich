package org.smdserver.words;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsStorage implements IWordsStorage
{
	private Map<String, List<Language> > usersWords;

	public WordsStorage ()
	{
		usersWords = new HashMap<String, List<Language> > ();
		//TODO: find a better place for it
		Word.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z"));
	}

	public void setUserWords (String userId, List<Language> languages)
	{
		usersWords.put(userId, languages);
	}

	public List<Language> getUserWords (String userId)
	{
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
		return usersWords.get(userId);
	}
}
