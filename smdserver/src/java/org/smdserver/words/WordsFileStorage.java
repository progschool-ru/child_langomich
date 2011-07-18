package org.smdserver.words;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class WordsFileStorage extends WordsStorage
{
	private String realPath;
	private long  lastModified;

	public WordsFileStorage (String realPath)
	{
		super();
		this.realPath = realPath;
	}

        @Override
        public void setUserWords (String userId, List<Language> languages)
	{
                super.setUserWords(userId, languages);
                try
		{
			SaveLanguages(userId, languages);
		}
		catch(Exception e){}
	}

        @Override
	public void addUserWords (String userId, List<Language> languages)
	{
                super.addUserWords(userId, languages);
                try
		{
			SaveLanguages(userId, getLanguages(userId));
		}
		catch(Exception e){}
	}

        @Override
	protected void checkUpdated(String userId)
	{
		File file = new File(realPath+"/"+userId+".dat");
		if(lastModified < file.lastModified())
			readFile(userId);
	}

	private void readFile(String userId)
	{
		try
                {
                        BufferedReader br = new BufferedReader(new FileReader(realPath+"/"+userId+".dat"));
                        String str = br.readLine();
                        JSONObject json = new JSONObject(str);
                        List<Language> languages = parseJSON(json.getJSONArray("languages"));
                        super.setUserWords(userId, languages);
                        
			File file = new File(realPath+"/"+userId+".dat");
			lastModified = file.lastModified();
		}
		catch(IOException ioe){}
                catch(JSONException e){}
		catch(WordsException e){}

	}

	private void SaveLanguages(String userId, List<Language> languages)  throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(realPath+"/"+userId+".dat"));
                try
                {
                    JSONObject object = new JSONObject();
                    object.put("languages", languages);
                
                    bw.write(object.toString());
                }
                catch (JSONException ex) {}
                bw.close();
	}
	private List<Language> parseJSON (JSONArray json) throws WordsException
	{
		List<Language> languages = new ArrayList<Language>();
		int length = json.length();

		try
		{
			for(int i = 0; i < length; i++)
			{
				JSONObject value = json.getJSONObject(i);
				languages.add(new Language(value));
			}
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}

		return languages;
	}
}