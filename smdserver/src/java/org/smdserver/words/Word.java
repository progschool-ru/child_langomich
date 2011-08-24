package org.smdserver.words;

import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.core.small.StringUtil;

public class Word
{
	private String original;
	private String translation;
	private int rating;

	public Word (String dirtyOriginal, String dirtyTranslation, int rating)
	{
		this.original = cleanWord(dirtyOriginal);
		this.translation = cleanWord(dirtyTranslation);
		this.rating = rating;
	}
	public Word (JSONObject json) throws WordsException
	{
		try
		{
			this.original = cleanWord(json.getString("original"));
			this.translation = cleanWord(json.getString("translation"));
			this.rating = json.getInt("rating");
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR, e, true);
		}
	}

	public String getOriginal ()
	{
		return original;
	}
	public int getRating ()
	{
		return rating;
	}
	public String getTranslation ()
	{
		return translation;
	}
	
	private String cleanWord(String dirtyValue)
	{
		return StringUtil.escapeHTML(dirtyValue);
	}
}
