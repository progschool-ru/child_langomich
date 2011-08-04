package org.smdserver.words;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordsStorageTest
{
	private WordsStorage storage;

    @Before
    public void setUp () throws ParseException
	{
		storage = new WordsStorage();
		List<Language> list = new ArrayList<Language>();

		list.add(new Language("enId", "en"));
		list.add(new Language("frId", "fr"));
		storage.setUserWords("1", list);

		list = new ArrayList<Language>();
		list.add(new Language("esId", "es"));
		storage.setUserWords("2", list);
    }

    @After
    public void tearDown ()
	{
		storage = null;
    }

	/**
	 * Test of setUserWords method, of class WordsStorage.
	 */
	@Test
	public void testSetUserWords ()
	{
		System.out.println("setUserWords");

		List<Language> languages = new ArrayList<Language>();
		languages.add(new Language("frId", "fr"));

		storage.setUserWords("3", languages);

		storage.setUserWords("1", new ArrayList<Language>());

		List<Language> first = storage.getUserWords("1");
		List<Language> third = storage.getUserWords("3");

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
		System.out.println("getUserWords");

		List<Language> first = storage.getUserWords("1");
		List<Language> second = storage.getUserWords("2");
		assertEquals("Languages in '1's list", 2, first.size());
		assertEquals("Lanugages in '2's list", 1, second.size());
		assertEquals("first language of '1' user", "en", first.get(0).getName());
		assertEquals("second language of '1' user", "fr", first.get(1).getName());
		assertEquals("first language of '2' user", "es", second.get(0).getName());
	}
}