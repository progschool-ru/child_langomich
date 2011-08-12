package org.smdserver.words;

import java.util.Calendar;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordTest
{
	@Test
	public void testParseJSONSuccess () throws WordsException, JSONException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+6"));
		calendar.set(Calendar.YEAR, 2010);
		calendar.set(Calendar.MONTH, Calendar.OCTOBER);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 23);

		String json = "{id:\"someUUID\",original:\"первый\",translation:\"first\",rating:4}";
		Word word = new Word(new JSONObject(json));

		assertEquals("original", "первый", word.getOriginal());
		assertEquals("translation", "first", word.getTranslation());
		assertEquals("rating", new Double(4), new Double(word.getRating()));
	}
}