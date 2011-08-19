package org.smdserver.words;

import org.json.JSONException;
import org.json.JSONObject;

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
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
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
	
	public static String cleanWord(String dirtyValue)
	{
		if(dirtyValue == null)
			return null;
		return dirtyValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;").trim();
	}
}
