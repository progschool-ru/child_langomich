package org.smdserver.words;

import java.util.List;
import org.smdserver.core.ISmdCoreFactory;

public class UserWords implements IUserWords
{
	private IWordsStorage wordsStorage;
	private String userId;
	
	public UserWords(ISmdCoreFactory factory, String userId)
	{
		wordsStorage = factory.createWordsStorage();
		this.userId = userId;
	}
	
	public List<Language> getLanguages()
	{
		return wordsStorage.getUserWords(userId);
	}
}
