package org.smdserver.words;

import com.ccg.util.JavaString;
import com.meterware.httpunit.WebRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.smdserver.core.WebActions;
import org.smdserver.core.WebParams;

import static org.junit.Assert.*;

public class DeleteWordsActionTest extends WordsTestBase
{
	@Test
	public void testDeleteWords() throws Exception
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
		
		WebRequest deleteReq = createActionRequest(WebActions.DELETE_WORDS);
		deleteReq.setParameter(WebParams.LANGUAGE_ID, LANGUAGE_ID);
		JSONArray words = new JSONArray();
		words.put(JavaString.encode(WORD_ORIG));
		deleteReq.setParameter(WebParams.WORDS, words.toString());
		JSONObject deleteObject = getJSONResource(getWC(), deleteReq);
		assertTrue(deleteObject.getBoolean(WebParams.SUCCESS));
		
		req = createActionRequest(WebActions.GET_WORDS);
		jsonObject = getJSONResource(getWC(), req);
		System.out.println(jsonObject.toString());
		languages = jsonObject.getJSONArray(KEY_LANGUAGES);
		language = new Language(languages.getJSONObject(0));

		assertTrue(jsonObject.getBoolean(WebParams.SUCCESS));
		assertEquals(1, languages.length());
		assertEquals(0, language.getWords().size());
	}
}
