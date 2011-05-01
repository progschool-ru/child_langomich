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

	public Word(String original, String translation, double rating, Date modified)
	{
		this.original = original;
		this.translation = translation;
		this.rating = rating;
		this.modified = modified;
	}

	public Word(JSONObject json) throws JSONException, ParseException
	{
		this.original = json.getString("original");
		this.translation = json.getString("translation");
		this.rating = json.getDouble("rating");

		this.modified = DateFormat.getDateInstance(DateFormat.SHORT).parse(json.getString("modified"));
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
}
