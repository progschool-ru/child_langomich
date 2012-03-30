package org.omich.lang.test;

import org.omich.lang.words.Word;

import android.test.AndroidTestCase;

public class WordsTests extends AndroidTestCase {
	
	private static final String ORIGINAL = "����";
	private static final String TRANSLATION = "Test";
	private static final int RATING = 0;
	private static final long MODIFIED = 1232323232;
	
	private static final String NEW_ORIGINAL = "�����";
	private static final String NEW_TRANSLATION = "NEW ";
	private static final int NEW_RATING = 4;
	private static final long NEW_MODIFIED = 32424343;
	public Word testWord;
	
	public WordsTests(){
		super();
	}
	
	@Override
	public void setUp() {
		testWord = new Word(ORIGINAL, TRANSLATION, RATING, MODIFIED);
	}
	
	public void testConstructor1(){
		Word word = new Word(ORIGINAL, TRANSLATION, RATING);
		assertEquals(word.getOriginal(), ORIGINAL);
		assertEquals(word.getTranslation(), TRANSLATION);
		assertEquals(word.getRating(), RATING);
		assertEquals(word.getModified(), 0);
	}
	
	public void testConstructor2(){
		Word word = new Word(ORIGINAL, TRANSLATION, RATING, MODIFIED);
		assertEquals(word.getOriginal(), ORIGINAL);
		assertEquals(word.getTranslation(), TRANSLATION);
		assertEquals(word.getRating(), RATING);
		assertEquals(word.getModified(), MODIFIED);
	}
	
	public void testSetOriginal(){
		testWord.setOriginal(NEW_ORIGINAL);
		assertEquals(testWord.getOriginal(), NEW_ORIGINAL);
	}
	
	public void testSetTranslation(){
		testWord.setTranslation(NEW_TRANSLATION);
		assertEquals(testWord.getTranslation(), NEW_TRANSLATION);
	}
	
	public void testSetRating(){
		testWord.setRating(NEW_RATING);
		assertEquals(testWord.getRating(), NEW_RATING);
	}
	
	public void testSetModified(){
		testWord.setModified(NEW_MODIFIED);
		assertEquals(testWord.getModified(), NEW_MODIFIED);
	}
}
