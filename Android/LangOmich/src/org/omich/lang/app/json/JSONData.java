package org.omich.lang.app.json;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.Word;

public class JSONData 
{
	private static final String SUCCESS = "success";
	private static final String SERVER_TIME = "serverTime";
	private static final String LANGUAGES = "languages";
	private static final String LAST_CONNECTION = "lastConnection";
	
	private long serverTime = 0;
	private boolean sucsess;
	private List<Word> words = new ArrayList<Word>();
	private List<Dict> dicts = new ArrayList<Dict>();
	
	public JSONData()
	{
		sucsess = false;
	}
	public JSONData(String jString) throws JSONException
	{
/*		
		JSONObject jObject = new JSONObject(jString);
		
		sucsess = jObject.getBoolean(SUCCESS);
		
		if(sucsess){
		
			if(jObject.has(LAST_CONNECTION)){
				lastconnect = jObject.getLong(LAST_CONNECTION);
			}
		
			JSONArray jLanguages = jObject.getJSONArray(LANGUAGES);
			
			for(int i=0; i<jLanguages.length(); i++){
				Language languge = new Language(jLanguages.getJSONObject(i));
				languages.add(languge);
			}
		}
		*/
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
						JSONWord.put("original",   words.get(j).nativ);		
						JSONWord.put("translation", words.get(j).foreign);
						JSONWord.put("rating",  words.get(j).rating);
//						JSONWord.put("time",    words.get(j).time);
						JSONWords.put(JSONWord);
					}
				}
				JSONDict.put("id",    dicts.get(i).serverId);		
				JSONDict.put("name",  dicts.get(i).name);				
				JSONDict.put("words", JSONWords);
				JSONDicts.put(JSONDict);
			}
			jObject.put(LANGUAGES, JSONDicts);
			jObject.put(LAST_CONNECTION, serverTime);
		}
		catch(JSONException e){}		

		return jObject.toString();
	}
}
