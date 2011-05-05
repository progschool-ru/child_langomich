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
	private double rating;
	private Date modified;

	private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

	public Word(String original, String translation, double rating, Date modified)
	{
		this.original = original;
		this.translation = translation;
		this.rating = rating;
		this.modified = modified;
	}

	public Word(JSONObject json) throws WordsException
	{
		try
		{
			this.original = json.getString("original");
			this.translation = json.getString("translation");
			this.rating = json.getDouble("rating");

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

	public Date getModified() {
		return modified;
	}

	public String getOriginal() {
		return original;
	}

	public double getRating() {
		return rating;
	}

	public String getTranslation() {
		return translation;
	}

	static void setDateFormat(DateFormat format)
	{
		dateFormat = format;
	}
}
