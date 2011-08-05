package org.smdserver.words;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WordsStorage implements IWordsStorage
{
	private Map<String, List<Language> > usersWords;
	protected SimpleDateFormat s;

	public WordsStorage ()
	{
		usersWords = new HashMap<String, List<Language> > ();
	}
	public boolean addUserWords (String userId, List<Language> languages)
	{
		checkUpdated(userId);
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
		for(int j = 0; j < languages.size(); j++)
		{
			boolean newLen = true;
			List<Language> storedLanguages = usersWords.get(userId);
			Language commingLanguage = languages.get(j);
			for(int i = 0; i < storedLanguages.size(); i++)
			{
				Language storedLanguage = storedLanguages.get(i);
				if(storedLanguage.getName().equals(commingLanguage.getName()))
				{
					newLen = false;
					List<Word> commingWords = commingLanguage.getWords();
					List<Word> storedWords = storedLanguage.getWords();
					for(int l = 0; l < commingWords.size(); l++)
					{
						boolean flag = true;
						Word commingWord = commingWords.get(l);
						for(int q = 0; q < storedWords.size(); q++)
						{
							Word storedWord = storedWords.get(q);
							if(commingWord.getOriginal().equals(storedWord.getOriginal()))
								if(commingWord.getModified() > storedWord.getModified())
								{
									storedWords.set(q, commingWord);
									flag = false;
									break;
								}
						}
						if(flag)
							storedWords.add(commingWord);
					}
					break;
				}
			}
			if(newLen)
				storedLanguages.add(commingLanguage);
		}
		return true;
	}
	public boolean setUserWords (String userId, List<Language> languages)
	{
		usersWords.put(userId, languages);
		return true;
	}
	protected void checkUpdated(String userId)
	{
	}
	protected List<Language> getLanguages(String userId)
	{
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
		return usersWords.get(userId);
	}
	public List<Language> getUserWords (String userId)
	{
		checkUpdated(userId);
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
		return usersWords.get(userId);
	}
	public List<Language> getCopyUserWords (String userId)
	{
		checkUpdated(userId);
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
		List<Language> languages =  new ArrayList<Language>();
		List<Language> storedLanguages = usersWords.get(userId);
        for(int j = 0; j < storedLanguages.size(); j++)
		{
			Language storedLanguage = storedLanguages.get(j);
			List<Word> storedWords = storedLanguage.getWords();
			//TODO: (3.low) create and use universal ID generator
			String uuid = UUID.randomUUID().toString();
			languages.add(new Language(uuid, storedLanguage.getName()));
			for(int i = 0; i < storedWords.size(); i++)
				languages.get(j).getWords().add(storedWords.get(i));
        }
		return languages;
	}
	public List<Language> getCopyUserWords (String userId, long lastModified)
	{
		checkUpdated(userId);
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
        List<Language> languages =  new ArrayList<Language>();
		List<Language> storedLanguages = usersWords.get(userId);
        for(int j = 0; j < storedLanguages.size(); j++)
		{
			Language storedLanguage = storedLanguages.get(j);
			languages.add(new Language(storedLanguage.getId(), storedLanguage.getName()));
			for(int i = 0; i < storedLanguage.getWords().size(); i++)
			{
				Word storedWord = storedLanguage.getWords().get(i);
				if(storedWord.getModified() > lastModified)
					languages.get(j).getWords().add(storedWord);
			}
        }
		return languages;
	}
}
