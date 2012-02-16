package org.omich.lang.test;

import org.omich.lang.words.Word;

import android.test.AndroidTestCase;

public class WordsTest  extends AndroidTestCase{
	
	public WordsTest (){
		super();
	}
	
	@Override
	public void setUp(){
		
	}
	
	public void test1(){
		Word word = new Word("тест","test", 3);
		assertEquals(word.getOriginal(), "тест");
		assertEquals(word.getTranslation(),"test");
		assertEquals(word.getRating(), 3);
	}
	

}
