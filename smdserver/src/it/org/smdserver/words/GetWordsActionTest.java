package org.smdserver.words;

import com.meterware.httpunit.WebRequest;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.smdserver.core.WebActions;
import org.smdserver.core.WebParams;

import static org.junit.Assert.*;

public class GetWordsActionTest extends WordsTestBase
{
	@Test
	public void testTest()
	{
		List<Language> list = getWordsStorage().getUserWords(USER_ID);
		assertEquals(1, list.size());
	}

	@Test
	public void testNoSpareSymbolsAtTheEnd() throws Exception
	{
		WebRequest req = createActionRequest(WebActions.GET_WORDS);
		String response = getTextResource(getWC(), req);
		assertEquals('}', response.charAt(response.length() - 1));
	}

	@Test
	public void testEncoded() throws Exception
	{
		WebRequest req = createActionRequest(WebActions.GET_WORDS);
		String response = getTextResource(getWC(), req);
		assertTrue(response.toLowerCase().contains("\\u043f\\u0435\\u0440\\u0432\\u044b\\u0439"));
	}

	@Test
	public void testGetWords() throws Exception
	{
		WebRequest req = createActionRequest(WebActions.GET_WORDS);
		JSONObject jsonObject = getJSONResource(getWC(), req);
		JSONArray languages = jsonObject.getJSONArray(KEY_LANGUAGES);
		Language language = new Language(languages.getJSONObject(0));
		Word word = language.getWords().get(0);

		assertTrue(jsonObject.getBoolean(WebParams.SUCCESS));
		assertEquals(1, languages.length());
		assertEquals(1, language.getWords().size());
		assertEquals(WORD_ORIG, word.getOriginal());
		assertEquals(WORD_TRAN, word.getTranslation());
		assertEquals(WORD_RATING, word.getRating());
	}
}
