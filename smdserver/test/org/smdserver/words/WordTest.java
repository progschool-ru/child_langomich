package org.smdserver.words;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordTest
{
	@Test
	public void testParseJSONSuccess() throws JSONException, ParseException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+6"));
		calendar.set(Calendar.YEAR, 2010);
		calendar.set(Calendar.MONTH, Calendar.OCTOBER);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 23);
		Date date = calendar.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");
		Word.setDateFormat(format);

		String json = "{original:\"������\",translation:\"first\",rating:4,modified:\"2010-10-01 10:00:23 +0600\"}";
		Word word = new Word(new JSONObject(json));

		assertEquals("original", "������", word.getOriginal());
		assertEquals("translation", "first", word.getTranslation());
		assertEquals("rating", new Double(4), new Double(word.getRating()));
		assertEquals("modified", format.format(date), format.format(word.getModified()));
	}
}