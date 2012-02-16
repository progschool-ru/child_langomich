package org.omich.lang.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.omich.lang.json.JSONWords;
import org.omich.lang.words.Word;

import android.test.AndroidTestCase;

public class JSONWordsTests extends AndroidTestCase {
	
	private String original;
	private String translation;
	private int rating;
	private long modified; 
	
	public JSONWordsTests(){
		super();
	}
	
	public void setUp(){
		String modified_str = "1309263860924";
		original = "тест";
		translation = "test";
		rating = 3;
		modified = Long.parseLong(modified_str);
	}
	
	public void WordToJSONTest() throws JSONException{
		Word word = new Word(original, translation, rating, modified);
		
		JSONObject jObject = JSONWords.wordToJSON(word);
		Word nWord = JSONWords.Parse(jObject);
		assertEquals(nWord.getOriginal(), original);
		assertEquals(nWord.getTranslation(),translation);
		assertEquals(nWord.getRating(), rating);
		assertEquals(nWord.getModified(), modified);
	}
}
