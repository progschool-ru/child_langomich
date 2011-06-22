package org.smdserver.words;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class Word
{
	private String original;
	private String translation;
	private int rating;
	private Date modified;

	private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

	public Word (String original, String translation, int rating, Date modified)
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
			this.modified = dateFormat.parse(json.getString("modified"));
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}
		catch(ParseException e)
		{
			throw new WordsException(WordsException.ICORRECT_DATE_FORMAT + "; " + e.getMessage());
		}
	}
	public Word (String word) throws WordsException
	{
		try
		{
                        String []arr = word.split(",");
			this.original = arr[0];
			this.translation = arr[1];
			this.rating = Integer.valueOf(arr[2]);
			this.modified = dateFormat.parse(arr[3]);
		}
		catch(ParseException e)
		{
			throw new WordsException(WordsException.ICORRECT_DATE_FORMAT + "; " + e.getMessage());
		}
	}
	public Date getModified ()
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

	static void setDateFormat (DateFormat format)
	{
		dateFormat = format;
	}
}
