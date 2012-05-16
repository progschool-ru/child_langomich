package org.omich.lang.test.word;

import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.words.Word;

import android.test.AndroidTestCase;

public class WordTest extends AndroidTestCase{
	
	
	String testData1 = "{\"translation\":\"and\",\"original\":\"è\",\"rating\":0}";
	String testData2 = "{\"translation\":\"and\",\"original\":\"è\",\"rating\":0, \"modified\":100500}";
	
	long id = -1;
	String original = "è";
	String translation = "and";
	int rating = 0;
	long modified1 = 0;
	long modified2 = 100500;
	int int_in_server = 0;
	boolean in_server = true;

	
	public WordTest(){
		super();
	}
		
	public void testJSONParse1() throws JSONException{
		
		JSONObject jObject= new JSONObject(testData1);
		Word word = new Word(jObject);
		
		assertEquals(word.getId(), id);
		assertEquals(word.getOriginal(), original);
		assertEquals(word.getTranslation(), translation);
		assertEquals(word.getRating(), rating);
		assertEquals(word.getModified(), modified1);
		assertEquals(word.getInServer(), in_server);
	}
	
	public void testJSONParse2() throws JSONException{
		JSONObject jObject= new JSONObject(testData2);
		Word word = new Word(jObject);
		
		assertEquals(word.getId(), id);
		assertEquals(word.getOriginal(), original);
		assertEquals(word.getTranslation(), translation);
		assertEquals(word.getRating(), rating);
		assertEquals(word.getModified(), modified2);
		assertEquals(word.getInServer(), in_server);
	}
	
	public void testToJSON() throws JSONException{
		Word word = new Word(id, original, translation, rating, modified2, int_in_server);
		String newData = word.toJSON().toString();
		
		JSONObject newJObject = new JSONObject(newData);
		Word newWord= new Word(newJObject);
		
		assertEquals(newWord.getId(), id);
		assertEquals(newWord.getOriginal(), original);
		assertEquals(newWord.getTranslation(), translation);
		assertEquals(newWord.getRating(), rating);
		assertEquals(newWord.getModified(), modified2);
		assertEquals(newWord.getInServer(), in_server);
	
	}
}
