package org.smdserver.words;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Language
{
	private String id;
	private String name;
	private long modified;
	private List<Word> words = new ArrayList<Word>();

	public Language (String id, String dirtyName, long modified)
	{
		this.id = id;
		this.name = cleanWord(dirtyName);
		this.modified = modified;
	}
	public Language (String id, String dirtyName, long modified, Word word)
	{
		this.id = id;
		this.name = cleanWord(dirtyName);
		this.modified = modified;
		words.add(word);
	}

	public Language ()
	{
	}
	
	public Language (JSONObject json) throws WordsException
	{
		try
		{
			id = json.has("id") ? json.getString("id") : null;
			name = cleanWord(json.getString("name"));
			modified = json.has("modified") ? json.getLong("modified") : 0;

			JSONArray wordJSONS = json.getJSONArray("words");
			int length = wordJSONS.length();
			for(int i = 0; i < length; i++)
			{
				Word word = new Word(wordJSONS.getJSONObject(i));
				words.add(word);
			}
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}
	}

	public String getName ()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String value)
	{
		id = value;
	}

	public long getModified()
	{
		return modified;
	}

	public List<Word> getWords ()
	{
		return words;
	}
	
	private static String cleanWord(String dirtyValue)
	{
		return Word.cleanWord(dirtyValue);
	}
}
