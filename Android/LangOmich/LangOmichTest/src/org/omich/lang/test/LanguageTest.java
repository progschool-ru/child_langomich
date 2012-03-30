package org.omich.lang.test;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.words.Language;
import org.omich.lang.words.Word;

import android.test.AndroidTestCase;

public class LanguageTest extends AndroidTestCase {
	
	private static final String NAME = "EN";
	private static final String ID = "word word word word";
	
	private static final String NEW_NAME = "FR";
	private static final String NEW_ID = "new id";
	
	private Language testLanguage;
	
	public LanguageTest(){
		super();
	}
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		
		testLanguage = new Language(NAME, ID, null);
	}
	
	public void testConstructor(){
		Language language = new Language(NAME, ID, null);
		
		assertEquals(language.getName(), NAME);
		assertEquals(language.getServerId(), ID);
		assertEquals(language.getWords(), null);
	}
	
	public void testSetName(){
		testLanguage.setName(NEW_NAME);
		assertEquals(testLanguage.getName(), NEW_NAME);
	}
	
	public void testSetId(){
		testLanguage.setServerId(NEW_ID);
		assertEquals(testLanguage.getServerId(), NEW_ID);
	}
	
	public void testSetWords(){
		List<Word> words = new ArrayList<Word>();
		testLanguage.setWords(words);
		assertEquals(testLanguage.getWords(), words);
	}
}
