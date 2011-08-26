package org.smdserver.words;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.smdserver.core.UsersTestBase;
import org.smdserver.core.WebActions;
import org.smdserver.core.WebParams;

import static org.junit.Assert.*;

public class WordsTestBase extends UsersTestBase
{
	protected static final String LANGUAGE_ID = "someOtherUUID";
	protected static final String LANGUAGE_NAME = "en";
	protected static final String WORD_ORIG = "первый";
	protected static final String WORD_TRAN = "first";
	protected static final int    WORD_RATING = 1;
	protected static final long   WORD_MODIFIED = 3000;
	
	protected static final String KEY_LANGUAGES = "languages";
	protected static final String KEY_NAME      = "name";
	protected static final String KEY_WORDS     = "words";
	
	private IWordsStorage wordsStorage;
	private WebConversation wc;
	
	@Before
	public void setUp() throws Exception
	{
		fillUsers();
		wordsStorage = getCore().getFactory().createWordsStorage();
		
		Word word = new Word(WORD_ORIG, WORD_TRAN, WORD_RATING);
		Language language = new Language(LANGUAGE_ID, LANGUAGE_NAME, word);
		List<Language> languages = new ArrayList<Language>();
		languages.add(language);
		wordsStorage.addUserWords(USER_ID, languages, WORD_MODIFIED);	
		
		wc = new WebConversation();
		
		WebRequest req = createActionRequest(WebActions.LOGIN);
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);
		JSONObject loginResponse = getJSONResource(wc, req);
		assertTrue(loginResponse.getBoolean(WebParams.SUCCESS));
	}
	
	@After
	public void tearDown() throws Exception
	{	
		wc = null;
		wordsStorage.setUserWords(USER_ID, new ArrayList<Language>());
		wordsStorage = null;
		clearUsers();	
	}
	
	protected IWordsStorage getWordsStorage()
	{
		return wordsStorage;
	}
	
	protected WebConversation getWC()
	{
		return wc;
	}
}
