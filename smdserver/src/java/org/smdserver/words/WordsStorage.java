package org.smdserver.words;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsStorage implements IWordsStorage
{
	private Map<String, List<Language> > usersWords;
        protected SimpleDateFormat s;

	public WordsStorage ()
	{
		usersWords = new HashMap<String, List<Language> > ();
	}
	public void addUserWords (String userId, List<Language> languages)
	{
                checkUpdated(userId);
            	if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
                for(int j = 0; j < languages.size(); j++) {
                    boolean newLen = true;
                    for(int i =0; i < usersWords.get(userId).size(); i++)
                        if(usersWords.get(userId).get(i).getName().equals(languages.get(j).getName()))
                        {
                            newLen = false;
                            for(int l = 0; l < languages.get(j).getWords().size(); l++)
                                usersWords.get(userId).get(i).getWords().add(languages.get(j).getWords().get(l));
                            break;
                        }
                    if(newLen == true)
                        usersWords.get(userId).add(languages.get(j));
                }
	}
	public void setUserWords (String userId, List<Language> languages)
	{
		usersWords.put(userId, languages);
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
	public List<Language> getUserWords (String userId, long lastModified)
	{
                checkUpdated(userId);
		if(!usersWords.containsKey(userId))
		{
			usersWords.put(userId, new ArrayList<Language>());
		}
                List<Language> languages =  new ArrayList<Language>();
                for(int j = 0; j < usersWords.get(userId).size(); j++) {
                    languages.add(new Language(usersWords.get(userId).get(j).getName()));
                    for(int i = 0; i < usersWords.get(userId).get(j).getWords().size(); i++)
                        if(usersWords.get(userId).get(j).getWords().get(i).getModified() > lastModified)
                        {
                            languages.get(j).getWords().add(usersWords.get(userId).get(j).getWords().get(i));
                        }
                }
		return languages;
	}
}
