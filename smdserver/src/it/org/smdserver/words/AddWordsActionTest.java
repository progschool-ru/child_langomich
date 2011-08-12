package org.smdserver.words;

import com.ccg.util.JavaString;
import org.smdserver.core.UsersTestBase;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smdserver.core.WebActions;
import org.smdserver.core.WebParams;

import static org.junit.Assert.*;

public class AddWordsActionTest extends UsersTestBase
{
	private static final String LANGUAGE_ID = "enId";
	private static final String LANGUAGE_NAME = "en";
//	private static final String LANGUAGE_ID2 = "frId";
	private static final String LANGUAGE_NAME2 = "fr";
	private static final String WORD_ORIG = "первый";
	private static final String WORD_TRAN = "first";
	private static final int    WORD_RATING = 1;
	private static final long   WORD_MODIFIED = 3000;
	private static final String WORD_ORIG2 = "второй";
	private static final String WORD_TRAN2 = "second";
	private static final int    WORD_RATING2 = 2;
	private static final long   WORD_MODIFIED2 = 4000;

	private static final String KEY_LANGUAGES = "languages";
	private static final String KEY_NAME      = "name";
	private static final String KEY_WORDS     = "words";

	IWordsStorage wordsStorage;
	private WebConversation wc;
	private boolean flag = true;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		UsersTestBase.setUpClass();
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
		UsersTestBase.tearDownClass();
	}

    @Before
    public void setUp() throws Exception
	{
		wordsStorage = getTestStorageHelper().openWordsStorage(getResource(), USER_ID);

		Word word = new Word(WORD_ORIG, WORD_TRAN, WORD_RATING, WORD_MODIFIED);
		Language language = new Language(LANGUAGE_ID, LANGUAGE_NAME, WORD_MODIFIED, word);
		List<Language> languages = new ArrayList<Language>();
		languages.add(language);
		wordsStorage.setUserWords(USER_ID, languages);

		wc = new WebConversation();

		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.LOGIN);
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);
		JSONObject loginResponse = getJSONResource(wc, req);
		assertTrue(loginResponse.getBoolean(WebParams.SUCCESS));
		flag = true;
	}

	@After
	public void tearDown()
	{
		if(flag)
		{
			getTestStorageHelper().closeWordsStorage(getResource(), USER_ID);
		}
		wc = null;
	}

	@Test
	public void testAddNewWordNewLanguage() throws Exception
	{
		WebRequest addReq = new GetMethodWebRequest(getActionUrl() + WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"name\":\"" + LANGUAGE_NAME2 + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG +
				"\",\"translation\":\"" + WORD_TRAN +
				"\",\"rating\":" + WORD_RATING +
				",\"modified\":" + WORD_MODIFIED + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));

		JSONObject addJSON = getJSONResource(wc, addReq);

		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = wordsStorage.getUserWords(USER_ID);
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
		assertEquals(WORD_MODIFIED, word.getModified());
	}

	@Test
	public void testAddNewWordExistedLanguage() throws Exception
	{
		WebRequest addReq = new GetMethodWebRequest(getActionUrl() + WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"id\":\"" + LANGUAGE_ID +
				"\",\"name\":\"" + LANGUAGE_NAME + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG2 +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING2 +
				",\"modified\":" + WORD_MODIFIED2 + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));
		JSONObject addJSON = getJSONResource(wc, addReq);
		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = wordsStorage.getUserWords(USER_ID);
		assertEquals(1, languages.size());

		Language language = languages.get(0);
		Word word = language.getWords().get(0).getOriginal().equals(WORD_ORIG2)
					? language.getWords().get(0)
					: language.getWords().get(1);
		assertEquals(2, language.getWords().size());
		assertEquals(WORD_ORIG2, word.getOriginal());
		assertEquals(WORD_TRAN2, word.getTranslation());
		assertEquals(WORD_RATING2, word.getRating());
		assertEquals(WORD_MODIFIED2, word.getModified());
	}

	@Test
	public void testModifyWord() throws Exception
	{
		WebRequest addReq = new GetMethodWebRequest(getActionUrl() + WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"id\":\"" + LANGUAGE_ID +
				"\",\"name\":\"" + LANGUAGE_NAME + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING2 +
				",\"modified\":" + WORD_MODIFIED2 + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));
		JSONObject addJSON = getJSONResource(wc, addReq);
		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = wordsStorage.getUserWords(USER_ID);
		assertEquals(1, languages.size());

		Language language = languages.get(0);
		Word word = language.getWords().get(0);
		assertEquals(1, language.getWords().size());
		assertEquals(WORD_ORIG, word.getOriginal());
		assertEquals(WORD_TRAN2, word.getTranslation());
		assertEquals(WORD_RATING2, word.getRating());
		assertEquals(WORD_MODIFIED2, word.getModified());
	}

	@Test
	public void testModifyAndAdd() throws Exception
	{
		WebRequest addReq = new GetMethodWebRequest(getActionUrl() + WebActions.ADD_WORDS);
		String param = "{\"languages\":[{\"id\":\"" + LANGUAGE_ID +
				"\",\"name\":\"" + LANGUAGE_NAME + "\",\"words\":" +
				"[{\"original\":\"" + WORD_ORIG2 +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING2 +
				",\"modified\":" + WORD_MODIFIED2 + "}," +
				"{\"original\":\"" + WORD_ORIG +
				"\",\"translation\":\"" + WORD_TRAN2 +
				"\",\"rating\":" + WORD_RATING +
				",\"modified\":" + WORD_MODIFIED2 + "}]}]}";
		addReq.setParameter(WebParams.DATA, JavaString.encode(param));
		JSONObject addJSON = getJSONResource(wc, addReq);
		assertTrue(addJSON.getBoolean(WebParams.SUCCESS));

		List<Language> languages = wordsStorage.getUserWords(USER_ID);
		assertEquals(1, languages.size());

		Language language = languages.get(0);
		Word wordModified = language.getWords().get(0).getOriginal().equals(WORD_ORIG)
					? language.getWords().get(0)
					: language.getWords().get(1);
		assertEquals(2, language.getWords().size());
		assertEquals(WORD_ORIG, wordModified.getOriginal());
		assertEquals(WORD_TRAN2, wordModified.getTranslation());
		assertEquals(WORD_RATING, wordModified.getRating());
		assertEquals(WORD_MODIFIED2, wordModified.getModified());

		Word wordAdded = language.getWords().get(0).getOriginal().equals(WORD_ORIG2)
					? language.getWords().get(0)
					: language.getWords().get(1);
		assertEquals(2, language.getWords().size());
		assertEquals(WORD_ORIG2, wordAdded.getOriginal());
		assertEquals(WORD_TRAN2, wordAdded.getTranslation());
		assertEquals(WORD_RATING2, wordAdded.getRating());
		assertEquals(WORD_MODIFIED2, wordAdded.getModified());
	}
}
