package org.smdserver.words;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Language
{
	private String name;
	private List<Word> words = new ArrayList<Word>();

	public Language (String name)
	{
		this.name = name;
	}

	public Language (JSONObject json) throws WordsException
	{
		try
		{
			name = json.getString("name");

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

	public List<Word> getWords ()
	{
		return words;
	}
}
