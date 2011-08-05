package org.smdserver.words;

import org.smdserver.core.UsersTestBase;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smdserver.core.WebActions;
import org.smdserver.core.WebParams;
import org.smdserver.core.ConsoleSmdLogger;

import static org.junit.Assert.*;

public class GetWordsActionTest extends UsersTestBase
{
	private static final String LANGUAGE_ID = "someOtherUUID";
	private static final String LANGUAGE_NAME = "en";
	private static final String WORD_ID = "someUUID";
	private static final String WORD_ORIG = "первый";
	private static final String WORD_TRAN = "first";
	private static final int    WORD_RATING = 1;
	private static final long   WORD_MODIFIED = 3000;

	private static final String KEY_LANGUAGES = "languages";
	private static final String KEY_NAME      = "name";
	private static final String KEY_WORDS     = "words";

	private WebConversation wc;
	private static WordsFileStorage wordsStorage;
	private static File file;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		UsersTestBase.setUpClass();
		file = new File(getResource().getString("test.server.path") + 
				             getResource().getString("path.words.storageDir") +
							 USER_ID + ".dat");
		wordsStorage = new WordsFileStorage(file.getParentFile().getAbsolutePath(),
											new ConsoleSmdLogger(System.out));

		Word word = new Word(WORD_ID, WORD_ORIG, WORD_TRAN, WORD_RATING, WORD_MODIFIED);
		Language language = new Language(LANGUAGE_ID, LANGUAGE_NAME, word);
		List<Language> languages = new ArrayList<Language>();
		languages.add(language);

		wordsStorage.addUserWords(USER_ID, languages);
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
		UsersTestBase.tearDownClass();
		file.delete();
		wordsStorage = null;
	}

    @Before
    public void setUp() throws Exception
	{
		wc = new WebConversation();
	
		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.LOGIN);
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);
		JSONObject loginResponse = getJSONResource(wc, req);
		assertTrue(loginResponse.getBoolean(WebParams.SUCCESS));
	}

	@After
	public void tearDown()
	{
		wc = null;
	}

	@Test
	public void testTest()
	{
		List<Language> list = wordsStorage.getUserWords(USER_ID);
		assertEquals(1, list.size());
	}

	@Test
	public void testNoSpareSymbolsAtTheEnd() throws Exception
	{
		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.GET_WORDS);
		String response = getTextResource(wc, req);
		assertEquals('}', response.charAt(response.length() - 1));
	}

	@Test
	public void testEncoded() throws Exception
	{
		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.GET_WORDS);
		String response = getTextResource(wc, req);
		assertTrue(response.toLowerCase().contains("\\u043f\\u0435\\u0440\\u0432\\u044b\\u0439"));
	}

	@Test
	public void testGetWords() throws Exception
	{
		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.GET_WORDS);
		JSONObject jsonObject = getJSONResource(wc, req);
		JSONArray languages = jsonObject.getJSONArray(KEY_LANGUAGES);
		Language language = new Language(languages.getJSONObject(0));
		Word word = language.getWords().get(0);

		assertTrue(jsonObject.getBoolean(WebParams.SUCCESS));
		assertEquals(1, languages.length());
		assertEquals(1, language.getWords().size());
		assertEquals(WORD_ORIG, word.getOriginal());
		assertEquals(WORD_TRAN, word.getTranslation());
		assertEquals(WORD_RATING, word.getRating());
		assertEquals(WORD_MODIFIED, word.getModified());
	}
}
