package org.smdserver.words;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smdserver.db.DBConfig;
import org.smdserver.db.IDBConfig;
import org.smdserver.util.ConsoleSmdLogger;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersDBStorage;
import static org.junit.Assert.*;

public class WordsDBStorageTest
{
	private static final String DELETE_USERS = "DELETE FROM %1$susers;";

	private static final String USER_ID_WITH_WORDS = "1";
	private static final String USER_ID_WITH_EMPTY_LANGUAGE = "2";
	private static final String USER_ID_WITHOUT_LANGUAGES = "3";
	private static final String EMAIL = "some@examle.org";
	private static final String ABOUT = "Petr Ivanov";

	private static final String LANGUAGE_ID = "enId1";
	private static final String LANGUAGE_NAME = "enId1Name";
	private static final String WORD_ORIGINAL = "enId1Word";
	private static final String WORD_TRANSLATION = "enId1WordTrans";

	private static final String EMPTY_LANGUAGE_ID = "esId";

	private static final long TIME_BEFORE = 10000;
	private static final long TIME_X      = 20000;
	private static final long TIME_AFTER  = 30000;
	
	private static final int RATING1 = 10;
	private static final int RATING2 = 20;

	ISmdDB db;
	IDBConfig config;
	private WordsDBStorage storage;

	@Before
	public void setUp () throws Exception
	{
		config = new DBConfig("org.smdserver.config", 
				                                "server.test.properties.file");

		db = new SmdDB(config, new ConsoleSmdLogger(System.out));

		IUsersStorage us = new UsersDBStorage(db);
		assertTrue(us.createUser(USER_ID_WITH_WORDS, "lo1", "pa1", EMAIL, ABOUT));
		assertTrue(us.createUser(USER_ID_WITH_EMPTY_LANGUAGE, "lo2", "pa2", EMAIL, ABOUT));
		assertTrue(us.createUser(USER_ID_WITHOUT_LANGUAGES, "lo3", "pa3", EMAIL, ABOUT));

		storage = new WordsDBStorage(db);
		List<Language> list = new ArrayList<Language>();

		list.add(new Language(LANGUAGE_ID, LANGUAGE_NAME, new Word(WORD_ORIGINAL, WORD_TRANSLATION, 1)));
		list.add(new Language("frId1", "fr", new Word("first", "первый", 1)));
		assertTrue(storage.setUserWords(USER_ID_WITH_WORDS, list, TIME_BEFORE));

		list = new ArrayList<Language>();
		list.add(new Language(EMPTY_LANGUAGE_ID, "es"));
		storage.setUserWords(USER_ID_WITH_EMPTY_LANGUAGE, list, TIME_BEFORE);
	}

	@After
	public void tearDown () throws Exception
	{
		db.close();

		String url = config.getDBUrl();
		String user = config.getDBUser();
		String password = config.getDBPassword();
		String prefix = config.getTablesPrefix();
		Connection connection = DriverManager.getConnection(url, user, password);
		connection.createStatement().executeUpdate(String.format(DELETE_USERS, prefix));
		storage = null;
	}
	
	@Test
	public void testDeleteWords()
	{
		List<Language> before = storage.getUserWords(USER_ID_WITH_WORDS);
		assertEquals(2, before.size());
		assertEquals(1, before.get(0).getWords().size());
		Word word1 = before.get(0).getWords().get(0);
		assertEquals(WORD_ORIGINAL, word1.getOriginal());
		assertEquals(WORD_TRANSLATION, word1.getTranslation());
		
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language(LANGUAGE_ID, LANGUAGE_NAME, new Word(WORD_ORIGINAL, "", 0)));
		storage.addUserWords(USER_ID_WITH_WORDS, languages, TIME_AFTER);
		
		List<Language> result = storage.getUserWords(USER_ID_WITH_WORDS);
		
		assertEquals(2, result.size());
		assertEquals(0, result.get(0).getWords().size());
		
		List<Language> resultBeforeBefore = storage.getLatestUserWords(USER_ID_WITH_WORDS, TIME_BEFORE / 2);
		assertEquals(2, resultBeforeBefore.size());
		assertEquals(1, resultBeforeBefore.get(0).getWords().size());
		Word word = resultBeforeBefore.get(0).getWords().get(0);
		assertEquals(WORD_ORIGINAL, word.getOriginal());
		assertEquals("", word.getTranslation());
		
		List<Language> resultX = storage.getLatestUserWords(USER_ID_WITH_WORDS, TIME_X);
		assertEquals(1, resultX.size());
		assertEquals(1, resultX.get(0).getWords().size());
		word = resultX.get(0).getWords().get(0);
		assertEquals(WORD_ORIGINAL, word.getOriginal());
		assertEquals("", word.getTranslation());
	}

	/**
	 * Test of setUserWords method, of class WordsStorage.
	 */
	@Test
	public void testSetUserWords ()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("frId3", "fr", new Word("first", "первый", RATING1)));

		storage.setUserWords(USER_ID_WITHOUT_LANGUAGES, languages);

		storage.setUserWords(USER_ID_WITH_WORDS, new ArrayList<Language>());

		List<Language> first = storage.getUserWords(USER_ID_WITH_WORDS);
		List<Language> third = storage.getUserWords(USER_ID_WITHOUT_LANGUAGES);

		assertNotNull(first);
		assertNotNull(third);
		assertEquals("User with id '1' should be replaced by new list", 0, first.size());
		assertEquals("Languaes in '3's list", 1, third.size());
		assertEquals("First '3's language", "fr", third.get(0).getName());
		assertEquals("first", third.get(0).getWords().get(0).getOriginal());
		assertEquals(RATING1, third.get(0).getWords().get(0).getRating());
	}

	/**
	 * Test of getUserWords method, of class WordsStorage.
	 */
	@Test
	public void testGetUserWords ()
	{
		List<Language> first = storage.getUserWords(USER_ID_WITH_WORDS);
		List<Language> second = storage.getUserWords(USER_ID_WITH_EMPTY_LANGUAGE);
		List<Language> third = storage.getUserWords(USER_ID_WITHOUT_LANGUAGES);
		assertEquals("Languages in '1's list", 2, first.size());
		assertEquals("Lanugages in '2's list", 1, second.size());
		assertEquals("Lanugages in '3's list", 0, third.size());
		assertEquals("first language id of '1' user", LANGUAGE_ID, first.get(0).getId());
		assertEquals("first language of '1' user", LANGUAGE_NAME, first.get(0).getName());
		assertEquals("second language of '1' user", "fr", first.get(1).getName());
		assertEquals("first language of '2' user", "es", second.get(0).getName());
	}

	@Test
	public void testRenameAndMerge()
	{
		//prepare language;
		List<Language> list = new ArrayList<Language>();
		list.add(new Language(EMPTY_LANGUAGE_ID, "someName", new Word("someWord", "someTranslation", 0)));
		storage.addUserWords(USER_ID_WITH_EMPTY_LANGUAGE, list, TIME_AFTER);
		assertEquals(1, storage.getUserWords(USER_ID_WITH_EMPTY_LANGUAGE).size());

		//test merge
		list = new ArrayList<Language>();
		list.add(new Language(null, "someName", new Word("anotherWord", "anotherTranslation", 0)));
		list.add(new Language(EMPTY_LANGUAGE_ID, "anotherName"));
		boolean success = storage.addUserWords(USER_ID_WITH_EMPTY_LANGUAGE, list, TIME_AFTER);

		List<Language> result = storage.getUserWords(USER_ID_WITH_EMPTY_LANGUAGE);

		assertTrue(success);
		assertEquals(2, result.size());

		Language l1 = result.get(0);
		Language l2 = result.get(1);
		if(l2.getId().equals(EMPTY_LANGUAGE_ID))
		{
			l2 = l1;
			l1 = result.get(1);
		}

		assertEquals(1, l1.getWords().size());
		assertEquals(1, l2.getWords().size());
		Word word1 = l1.getWords().get(0);
		Word word2 = l2.getWords().get(0);
		assertEquals(EMPTY_LANGUAGE_ID, l1.getId());
		assertEquals("Old language should be renamed",      "anotherName", l1.getName());
		assertEquals("Old language keeps old word",         "someWord", word1.getOriginal());
		assertEquals("There is new language with new name", "someName", l2.getName());
		assertEquals("New word belongs to new language",    "anotherWord", word2.getOriginal());
	}

	@Test
	public void testMergeLanguages()
	{
		//prepare language;
		List<Language> list = new ArrayList<Language>();
		list.add(new Language(EMPTY_LANGUAGE_ID, "someName", new Word("someWord", "someTranslation", 0)));
		storage.addUserWords(USER_ID_WITH_EMPTY_LANGUAGE, list, TIME_AFTER);
		assertEquals(1, storage.getUserWords(USER_ID_WITH_EMPTY_LANGUAGE).size());

		//test merge
		list = new ArrayList<Language>();
		list.add(new Language(null, "someName", new Word("anotherWord", "anotherTranslation", 0)));
		boolean success = storage.addUserWords(USER_ID_WITH_EMPTY_LANGUAGE, list, TIME_AFTER);

		List<Language> result = storage.getUserWords(USER_ID_WITH_EMPTY_LANGUAGE);

		assertTrue(success);
		assertEquals(1, result.size());

		Language l = result.get(0);
		assertEquals(2, l.getWords().size());

		Word word0 = l.getWords().get(0);
		Word word1 = l.getWords().get(1);
		assertTrue(word0.getOriginal().equals("someWord") && word1.getOriginal().equals("anotherWord") ||
					word1.getOriginal().equals("someWord") && word0.getOriginal().equals("anotherWord"));
	}

	@Test
	public void testAddToAnotherUser()
	{
		List<Language> list = new ArrayList<Language>();
		list.add(new Language(EMPTY_LANGUAGE_ID, "someName", new Word("someWord", "someTranslation", 0)));
		list.add(new Language("someLanguageId", "someLanguageName", new Word("anotherWord", "anotherTranslation", 0)));
		
		boolean success = storage.addUserWords(USER_ID_WITHOUT_LANGUAGES, list, TIME_AFTER);

		List<Language> result1 = storage.getUserWords(USER_ID_WITH_EMPTY_LANGUAGE);
		List<Language> result2 = storage.getUserWords(USER_ID_WITHOUT_LANGUAGES);

		assertFalse(success);
		assertEquals("There is still one language for that user", 1, result1.size());
		assertEquals("There is still empty language", 0, result1.get(0).getWords().size());
		assertEquals("There are still no languages for that user", 0, result2.size());
	}
	
	@Test
	public void testAddDirtyWords()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("frId3", "fr</table>", new Word("first</table>", "first", 1)));
		languages.add(new Language("frId4", "f&lt;/table&gt;", new Word("first&lt;/table&GT;", "second<>", 1)));

		storage.setUserWords(USER_ID_WITHOUT_LANGUAGES, languages);

		List<Language> third = storage.getUserWords(USER_ID_WITHOUT_LANGUAGES);
		
		Language firstLanguage = third.get(0);
		Language secondLanguage;
		
		if(firstLanguage.getName().equals("fr&lt;/table&gt;"))
		{
			secondLanguage = third.get(1);
		}
		else
		{
			secondLanguage = firstLanguage;
			firstLanguage = third.get(1);
		}
		
		Word firstWord = firstLanguage.getWords().get(0);
		Word secondWord = secondLanguage.getWords().get(0);

		assertNotNull(third);
		assertEquals("Languaes in '3's list", 2, third.size());
		assertEquals("First '3's language", "fr&lt;/table&gt;", firstLanguage.getName());		
		assertEquals("First '3's language", "f&lt;/table&gt;", secondLanguage.getName());		
		assertEquals("first&lt;/table&gt;", firstWord.getOriginal());		
		assertEquals("first&lt;/table&GT;", secondWord.getOriginal());	
		assertEquals("second&lt;&gt;", secondWord.getTranslation());	
		
	}

	@Test
	public void testAddEmptyLanguage()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("frId3", "fr"));
		storage.setUserWords(USER_ID_WITHOUT_LANGUAGES, languages);

		List<Language> third = storage.getUserWords(USER_ID_WITHOUT_LANGUAGES);

		assertEquals(1, third.size());

		Language language = third.get(0);
		assertEquals("fr", language.getName());
		assertEquals(0, language.getWords().size());
	}

	@Test
	public void testAddEmptyListOfLanguages()
	{
		List<Language> languages = new ArrayList<Language>();
		try
		{
			storage.setUserWords(USER_ID_WITHOUT_LANGUAGES, languages);
		}
		catch(Exception e)
		{
			assertTrue("Shouldn't throw exception", false);
		}
	}

	@Test
	public void testGetModifiedLanguageWithOlderWords()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("someNewId", "someName", new Word("someWord", "someTranslation", 0)));
		storage.addUserWords(USER_ID_WITHOUT_LANGUAGES, languages, TIME_X);

		List<Language> result = storage.getLatestUserWords(USER_ID_WITHOUT_LANGUAGES, TIME_X);

		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getWords().size());
	}

	@Test
	public void testEditWords()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language(EMPTY_LANGUAGE_ID, "oldName", new Word("someWord", "someTranslation", RATING1)));
		languages.add(new Language("someNewId", "someName"));
		storage.addUserWords(USER_ID_WITH_EMPTY_LANGUAGE, languages, TIME_BEFORE);

		languages = new ArrayList<Language>();
		languages.add(new Language("someNewId", "someName"));
		languages.add(new Language(EMPTY_LANGUAGE_ID, "oldName", new Word("someWord", "someOtherTranslation", RATING2)));
		storage.addUserWords(USER_ID_WITH_EMPTY_LANGUAGE, languages, TIME_AFTER);

		List<Language> result = storage.getLatestUserWords(USER_ID_WITH_EMPTY_LANGUAGE, TIME_X);
		assertEquals(1, result.size());
		List<Word> words = result.get(0).getWords();
		assertEquals(1, words.size());
		assertEquals("someOtherTranslation", words.get(0).getTranslation());
		assertEquals(RATING2, words.get(0).getRating());
	}

	@Test
	public void testGetLatestUserWords()
	{
		List<Language> languagesB = new ArrayList<Language>();
		List<Language> languagesA = new ArrayList<Language>();
		List<Language> languagesAA = new ArrayList<Language>();
		languagesB.add(new Language("getLatest1", "getLatest1"));
		languagesA.add(new Language("getLatest2", "getLatest2"));
		languagesA.add(new Language(LANGUAGE_ID, "oldName", new Word("getLatestWord1", "getLatestWord1", 0)));
		
		Language languageA = new Language("getLatest3", "getLatest3");
		Language languageAA = new Language("getLatest3", "getLatest3");
		languageAA.getWords().add(new Word("getLatestWord2", "getLatestWord2", 0));
		languageA.getWords().add(new Word("getLatestWord3", "getLatestWord3", 0));

		languagesA.add(languageA);
		languagesAA.add(languageAA);

		storage.addUserWords(USER_ID_WITH_WORDS, languagesB, TIME_BEFORE);
		storage.addUserWords(USER_ID_WITH_WORDS, languagesA, TIME_AFTER);
		storage.addUserWords(USER_ID_WITH_WORDS, languagesAA, TIME_BEFORE);

		List<Language> result = storage.getLatestUserWords(USER_ID_WITH_WORDS, TIME_X);
		assertEquals("There are should be three languages: " +
						"LANGUAGE_ID with getLatestWord1, " +
						"getLatest2 without words and " +
						"getLatest3 with 1 word", 
					3, result.size());

		Language[] langs;
		if(null == (langs = helpToOrderLanguages(result.get(0), result.get(1), result.get(2))))
		{
			if(null == (langs = helpToOrderLanguages(result.get(1), result.get(2), result.get(0))))
			{
				langs = helpToOrderLanguages(result.get(2), result.get(0), result.get(1));
			}
		}

		assertNotNull(langs);
		assertEquals(1, langs[0].getWords().size());
		assertEquals(0, langs[1].getWords().size());
		assertEquals(1, langs[2].getWords().size());
	}

	private Language[] helpToOrderLanguages(Language lang1, Language lang2, Language lang3)
	{
		if(lang1.getId().equals(LANGUAGE_ID))
		{
			Language[] languages = new Language[3];
			languages[0] = lang1;
			if(lang2.getId().equals("getLatest2"))
			{
				languages[1] = lang2;
				languages[2] = lang3;
			}
			else
			{
				languages[1] = lang3;
				languages[2] = lang2;
			}
			return languages;
		}
		else
		{
			return null;
		}
	}
}
