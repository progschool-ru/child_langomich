package org.smdserver.words;

import java.text.SimpleDateFormat;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class LanguageTest
{
	@Test
	public void testParseJSONSuccess() throws WordsException, JSONException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");
		Word.setDateFormat(format);

		String json = "{name:\"en\",words:[" +
				"{original:\"первый\",translation:\"first\",rating:4," +
				"modified:\"2010-10-01 10:00:23 +0600\"}," +
				"{original:\"второй\",translation:\"second\",rating:0," +
				"modified:\"2011-10-01 10:10:23 +0600\"}" +
				"]}";

		Language language = new Language(new JSONObject(json));

		assertEquals("language name", "en", language.getName());
		assertEquals("words count", 2, language.getWords().size());

		Word first = language.getWords().get(0);
		Word second = language.getWords().get(1);

		assertEquals("original", "первый", first.getOriginal());
		assertEquals("translation", "first", first.getTranslation());
		assertEquals("rating", new Double(4), new Double(first.getRating()));

		assertEquals("original", "второй", second.getOriginal());
		assertEquals("translation", "second", second.getTranslation());
		assertEquals("rating", new Double(0), new Double(second.getRating()));
	}
}