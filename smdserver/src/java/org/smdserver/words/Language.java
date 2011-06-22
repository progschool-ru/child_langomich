package org.smdserver.words;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Language
{
	private String name;
	private List<Word> words = new ArrayList<Word>();

	public Language (String str)
	{

		String []arr = str.split("@@@");
                if(arr.length < 2)
                    name = str;
                else {
                    name = arr[0];
                    str = arr[1];
                    String []arr2 = str.split("     ");
                    for(int i = 0; i < arr2.length; i++)
                    {
                        try{

                            Word word = new Word(arr2[i]);
                            words.add(word);
                        }
                        catch(WordsException we){}
                    }
                }
	}
	public Language (String name, Word word)
	{
                this.name = name;
                words.add(word);
	}
        public Language ()
	{
	}
	public Language (JSONObject json) throws WordsException
	{
		try
		{
			name = json.getString("name");

			JSONArray wordJSONS = json.getJSONArray("words");
			int length = wordJSONS.length();
			for(int i = 0; i < length; i++)
			{
				Word word = new Word(wordJSONS.getJSONObject(i));
				words.add(word);
			}
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}
	}

	public String getName ()
	{
		return name;
	}

	public List<Word> getWords ()
	{
		return words;
	}
}
