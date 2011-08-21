package org.smdserver.words;

import com.ccg.util.JavaString;
import com.meterware.httpunit.WebRequest;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.WebActions;
import org.smdserver.core.WebParams;

import static org.junit.Assert.*;

public class AddWordsActionTest extends WordsTestBase
{
	private static final String LANGUAGE_NAME2 = "fr";

	private static final String WORD_ORIG2 = "второй";
	private static final String WORD_TRAN2 = "second";
	private static final int    WORD_RATING2 = 2;

	@Test
	public void testDeviceAddsNewLanguage() throws Exception
	{
		WebRequest deviceReq = createActionRequest(WebActions.ADD_WORDS);
		JSONObject paramsJSON = new JSONObject();
		paramsJSON.put(ActionParams.LANGUAGES, new JSONArray());
		deviceReq.setParameter(WebParams.DATA, paramsJSON.toString());

		JSONObject deviceJSON = getJSONResource(getWC(), deviceReq);
		long lastConnection = deviceJSON.getLong(ActionParams.LAST_CONNECTION);


		WebRequest addReq = createActionRequest(WebActions.ADD_WORDS);
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language(null, LANGUAGE_NAME2, new Word("someWord", "someTranslation", 0)));
		paramsJSON = new JSONObject();
		paramsJSON.put(ActionParams.LAST_CONNECTION, lastConnection);
		paramsJSON.put(ActionParams.LANGUAGES, languages);
		addReq.setParameter(WebParams.DATA, paramsJSON.toString());

		JSONObject addJSON = getJSONResource(getWC(), addReq);
		JSONArray addLanguages = addJSON.has(ActionParams.LANGUAGES) ? addJSON.getJSONArray(ActionParams.LANGUAGES) : null;
		lastConnection = addJSON.getLong(ActionParams.LAST_CONNECTION);

		assertNotNull(addLanguages);
		assertEquals(1, addLanguages.length());
		assertEquals(LANGUAGE_NAME2, addLanguages.getJSONObject(0).getString("name"));
		assertEquals(0, addLanguages.getJSONObject(0).getJSONArray("words").length());

		
		WebRequest checkReq = createActionRequest(WebActions.ADD_WORDS);
		paramsJSON = new JSONObject();
		paramsJSON.put(ActionParams.LAST_CONNECTION, lastConnection);
		paramsJSON.put(ActionParams.LANGUAGES, new JSONArray());
		checkReq.setParameter(WebParams.DATA, paramsJSON.toString());

		JSONObject checkJSON = getJSONResource(getWC(), checkReq);
		JSONArray checkLanguages = checkJSON.has(ActionParams.LANGUAGES) ? checkJSON.getJSONArray(ActionParams.LANGUAGES) : null;

		assertTrue(checkLanguages == null || checkLanguages.length() == 0);
	}

	@Test
	public void testAddNewWordNewLanguage() throws Exception
	{
		WebRequest addReq = createActionRequest(WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"name\":\"" + LANGUAGE_NAME2 + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG +
				"\",\"translation\":\"" + WORD_TRAN +
				"\",\"rating\":" + WORD_RATING + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));

		JSONObject addJSON = getJSONResource(getWC(), addReq);

		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = getWordsStorage().getUserWords(USER_ID);
		assertEquals(2, languages.size());

		Language language;
		if(languages.get(0).getName().equals(LANGUAGE_NAME2))
		{
			language = languages.get(0);
		}
		else
		{
			language = languages.get(1);
		}
		Word word = language.getWords().get(0);
		assertEquals(1, language.getWords().size());
		assertEquals(WORD_ORIG, word.getOriginal());
		assertEquals(WORD_TRAN, word.getTranslation());
		assertEquals(WORD_RATING, word.getRating());
	}

	@Test
	public void testAddNewWordExistedLanguage() throws Exception
	{
		WebRequest addReq = createActionRequest(WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"id\":\"" + LANGUAGE_ID +
				"\",\"name\":\"" + LANGUAGE_NAME + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG2 +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING2 + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));
		JSONObject addJSON = getJSONResource(getWC(), addReq);
		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = getWordsStorage().getUserWords(USER_ID);
		assertEquals(1, languages.size());

		Language language = languages.get(0);
		Word word = language.getWords().get(0).getOriginal().equals(WORD_ORIG2)
					? language.getWords().get(0)
					: language.getWords().get(1);
		assertEquals(2, language.getWords().size());
		assertEquals(WORD_ORIG2, word.getOriginal());
		assertEquals(WORD_TRAN2, word.getTranslation());
		assertEquals(WORD_RATING2, word.getRating());
	}

	@Test
	public void testModifyWord() throws Exception
	{
		WebRequest addReq = createActionRequest(WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"id\":\"" + LANGUAGE_ID +
				"\",\"name\":\"" + LANGUAGE_NAME + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING2 + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));
		JSONObject addJSON = getJSONResource(getWC(), addReq);
		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = getWordsStorage().getUserWords(USER_ID);
		assertEquals(1, languages.size());

		Language language = languages.get(0);
		Word word = language.getWords().get(0);
		assertEquals(1, language.getWords().size());
		assertEquals(WORD_ORIG, word.getOriginal());
		assertEquals(WORD_TRAN2, word.getTranslation());
		assertEquals(WORD_RATING2, word.getRating());
	}

	@Test
	public void testModifyAndAdd() throws Exception
	{
		WebRequest addReq = createActionRequest(WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"id\":\"" + LANGUAGE_ID +
				"\",\"name\":\"" + LANGUAGE_NAME + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG2 +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING2 + "}," +
				"{\"original\":\"" + WORD_ORIG +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING +
				"}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));
		JSONObject addJSON = getJSONResource(getWC(), addReq);
		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = getWordsStorage().getUserWords(USER_ID);
		assertEquals(1, languages.size());

		Language language = languages.get(0);
		Word wordModified = language.getWords().get(0).getOriginal().equals(WORD_ORIG)
					? language.getWords().get(0)
					: language.getWords().get(1);
		assertEquals(2, language.getWords().size());
		assertEquals(WORD_ORIG, wordModified.getOriginal());
		assertEquals(WORD_TRAN2, wordModified.getTranslation());
		assertEquals(WORD_RATING, wordModified.getRating());

		Word wordAdded = language.getWords().get(0).getOriginal().equals(WORD_ORIG2)
					? language.getWords().get(0)
					: language.getWords().get(1);
		assertEquals(2, language.getWords().size());
		assertEquals(WORD_ORIG2, wordAdded.getOriginal());
		assertEquals(WORD_TRAN2, wordAdded.getTranslation());
		assertEquals(WORD_RATING2, wordAdded.getRating());
	}
}
