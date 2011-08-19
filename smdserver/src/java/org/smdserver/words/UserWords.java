package org.smdserver.words;

import java.util.List;
import org.smdserver.core.ISmdCore;

public class UserWords implements IUserWords
{
	private IWordsStorage wordsStorage;
	private String userId;
	
	public UserWords(ISmdCore core, String userId)
	{
		wordsStorage = core.createWordsStorage();
		this.userId = userId;
	}
	
	public List<Language> getLanguages()
	{
		return wordsStorage.getUserWords(userId);
	}
}
