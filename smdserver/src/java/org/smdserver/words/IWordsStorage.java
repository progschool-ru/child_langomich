package org.smdserver.words;

import java.util.List;

public interface IWordsStorage
{
	public List<Language> getUserWords (String userId);
	public List<Language> getLanguages(String userId);
	public List<Language> getLatestUserWords(String userId, long lastModified);
	public boolean setUserWords (String userId, List<Language> languages);
    public boolean addUserWords (String userId, List<Language> languages, long currentDeviceTime);
}
