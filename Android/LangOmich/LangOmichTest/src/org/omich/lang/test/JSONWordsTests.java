package org.omich.lang.test;

import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.json.JSONWords;
import org.omich.lang.words.Word;

import android.test.AndroidTestCase;

public class JSONWordsTests extends AndroidTestCase {
	
	private static final String ORIGINAL = "original";
	private static final String TRANSLATION = "translation";
	private static final String RATING = "rating";
	private static final String MODIFIED = "modified";
	
	private static final String original = "оригинал";
	private static final String translation = "Hello";
	private static int rating = 0;
	private static long modified = 123434341;
	
	private JSONObject jWord;
	private JSONArray jWords;
	
	public JSONWordsTests(){
		super();
	}
	
	@Override
	public void setUp() throws Exception{
		
		super.setUp();
		
		jWord = new JSONObject();
		jWord.put(ORIGINAL, original);
		jWord.put(TRANSLATION, translation);
		jWord.put(RATING, rating);
		jWord.put(MODIFIED, modified);
		
		jWords = new JSONArray();
		jWords.put(jWord);
		jWords.put(jWord);
		
	}
	
	public void testWordParser() throws JSONException{
		
		Word word  = JSONWords.parseWord(jWord);
		
		assertEquals(word.getOriginal(), original);
		assertEquals(word.getTranslation(), translation);
		assertEquals(word.getRating(), rating);
		assertEquals(word.getModified(), modified);
		
	}
	
	public void testWordsParser() throws JSONException{
		
		List<Word> words = JSONWords.parse(jWords);
		
		ListIterator<Word> iter = words.listIterator();
		
		int length = 0;
		
		while(iter.hasNext()){
			
			length++;
			
			Word word = iter.next();
			
			assertEquals(word.getOriginal(), original);
			assertEquals(word.getTranslation(), translation);
			assertEquals(word.getRating(), rating);
			assertEquals(word.getModified(), modified);
		}
		
		assertEquals(length, 2);
	}
	
	

}