package org.smdserver.words;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smdserver.core.ConsoleSmdLogger;
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

	ISmdDB db;
	ResourceBundle rb;
	private WordsDBStorage storage;

    @Before
    public void setUp () throws Exception
	{
		String testConfig = ResourceBundle.getBundle("org.smdserver.config")
				                      .getString("server.test.properties.file");
		rb = ResourceBundle.getBundle(testConfig);

		db = new SmdDB(rb, new ConsoleSmdLogger(System.out));
		String prefix = rb.getString("db.tablesPrefix");

		IUsersStorage us = new UsersDBStorage(db, prefix);
		assertTrue(us.createUser(USER_ID_WITH_WORDS, "lo1", "pa1"));
		assertTrue(us.createUser(USER_ID_WITH_EMPTY_LANGUAGE, "lo2", "pa2"));
		assertTrue(us.createUser(USER_ID_WITHOUT_LANGUAGES, "lo3", "pa3"));

		storage = new WordsDBStorage(db, prefix);
		List<Language> list = new ArrayList<Language>();

		list.add(new Language("enId1", "en", new Word("first", "первый", 1, 1)));
		list.add(new Language("frId1", "fr", new Word("first", "первый", 1, 1)));
		assertTrue(storage.setUserWords(USER_ID_WITH_WORDS, list));

		list = new ArrayList<Language>();
		list.add(new Language("esId", "es"));
		storage.setUserWords(USER_ID_WITH_EMPTY_LANGUAGE, list);
	}

   @After
    public void tearDown () throws Exception
	{
		db.close();

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		String prefix = rb.getString("db.tablesPrefix");
		Connection connection = DriverManager.getConnection(url, user, password);
		connection.createStatement().executeUpdate(String.format(DELETE_USERS, prefix));
		storage = null;
    }

	/**
	 * Test of setUserWords method, of class WordsStorage.
	 */
	@Test
	public void testSetUserWords ()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("frId3", "fr", new Word("first", "первый", 1, 1)));

		storage.setUserWords(USER_ID_WITHOUT_LANGUAGES, languages);

		storage.setUserWords(USER_ID_WITH_WORDS, new ArrayList<Language>());

		List<Language> first = storage.getUserWords(USER_ID_WITH_WORDS);
		List<Language> third = storage.getUserWords(USER_ID_WITHOUT_LANGUAGES);

		assertNotNull(first);
		assertNotNull(third);
		assertEquals("User with id '1' should be replaced by new list", 0, first.size());
		assertEquals("Languaes in '3's list", 1, third.size());
		assertEquals("First '3's language", "fr", third.get(0).getName());
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
		assertEquals("first language of '1' user", "en", first.get(0).getName());
		assertEquals("second language of '1' user", "fr", first.get(1).getName());
		assertEquals("first language of '2' user", "es", second.get(0).getName());
	}
	
	@Test
	public void testAddDirtyWords()
	{
		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("frId3", "fr</table>", new Word("first</table>", "first", 1, 1)));
		languages.add(new Language("frId4", "f&lt;/table&gt;", new Word("first&lt;/table&GT;", "second<>", 1, 1)));

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
	public void testAddEmptyLanguagesList()
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
}
