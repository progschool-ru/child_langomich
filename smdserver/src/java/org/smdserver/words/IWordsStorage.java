package org.smdserver.words;

import java.util.List;

public interface IWordsStorage
{
	public List<Language> getUserWords(String userId);
	public void setUserWords(String userId, List<Language> languages);
}
