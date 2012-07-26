package org.omich.lang.app.json;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IWStorage;
import org.omich.lang.app.db.Word;

public class JSONData 
{
	private static final String SUCCESS			= "success";
	private static final String LANGUAGES		= "languages";
	private static final String LAST_CONNECTION = "lastConnection";
	private static final String ID				= "id";
	private static final String NAME			= "name";
	private static final String WORDS			= "words";
	private static final String NATIV			= "translation";
	private static final String FOREIGN			= "original";
	private static final String RATING			= "rating";
	
	private long serverTime = 0;
	private boolean sucsess;
	private List<Word> words = new ArrayList<Word>();
	private List<Dict> dicts = new ArrayList<Dict>();
	
	public JSONData()
	{
		sucsess = false;
	}
	public long write(String jString, IWStorage mDbW) throws JSONException
	{		
		JSONObject jObject = new JSONObject(jString);
		
		sucsess = jObject.getBoolean(SUCCESS);
		serverTime = 0;
		if(sucsess)
		{		
			if(jObject.has(LAST_CONNECTION))
			{
				serverTime = jObject.getLong(LAST_CONNECTION);
			}	
			JSONArray JSONLanguages = jObject.getJSONArray(LANGUAGES);	
			for(int i = 0; i < JSONLanguages.length(); i++)
			{
				String serverId = JSONLanguages.getJSONObject(i).getString(ID);
				String name = JSONLanguages.getJSONObject(i).getString(NAME);	
				long id = mDbW.addDict(serverId, name);
				JSONArray JSONWords = JSONLanguages.getJSONObject(i).getJSONArray(WORDS);
				for(int j = 0; j < JSONWords.length(); j++)
				{
					String nativ 	= JSONWords.getJSONObject(j).getString(NATIV);
					String foreign	= JSONWords.getJSONObject(j).getString(FOREIGN);					
					int rating		= JSONWords.getJSONObject(j).getInt(RATING);
					mDbW.addWord(nativ, foreign, rating, id);
				}
			}
			
		}
		return serverTime;		
	}
	public void put(List<Word> words, List<Dict> dicts, long serverTime)
	{
		this.words = words;
		this.dicts = dicts;
		this.serverTime = serverTime;
	}
	
	@Override
	public String toString()
	{
		JSONObject jObject = new JSONObject();
		try
		{
			JSONArray JSONDicts = new JSONArray();
			for(int i = 0; i < dicts.size();i++)
			{
				JSONObject JSONDict = new JSONObject();
				JSONArray JSONWords = new JSONArray();
				for(int j = 0; j < words.size();j++)
				{
					if(words.get(j).id == dicts.get(i).dictId)
					{
						JSONObject JSONWord = new JSONObject();
						JSONWord.put(NATIV,			words.get(j).nativ);		
						JSONWord.put(FOREIGN,		words.get(j).foreign);
						JSONWord.put(RATING,		words.get(j).rating);
						JSONWords.put(JSONWord);
					}
				}
				JSONDict.put(ID,    dicts.get(i).serverId);	
				JSONDict.put(NAME,  dicts.get(i).name);				
				JSONDict.put(WORDS, JSONWords);
				JSONDicts.put(JSONDict);
			}
			jObject.put(LANGUAGES, JSONDicts);
			jObject.put(LAST_CONNECTION, serverTime);
		}
		catch(JSONException e){}		

		return jObject.toString();
	}
}
