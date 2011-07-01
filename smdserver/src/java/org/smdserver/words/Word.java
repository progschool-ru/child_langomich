package org.smdserver.words;

import org.json.JSONException;
import org.json.JSONObject;

public class Word
{
	private String original;
	private String translation;
	private int rating;
	private long modified;

	public Word (String original, String translation, int rating, long modified)
	{
		this.original = original;
		this.translation = translation;
		this.rating = rating;
		this.modified = modified;
	}
	public Word (JSONObject json) throws WordsException
	{
		try
		{
			this.original = json.getString("original");
			this.translation = json.getString("translation");
			this.rating = json.getInt("rating");
			this.modified = json.getLong("modified");
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}
	}
	public long getModified ()
	{
		return modified;
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
}
