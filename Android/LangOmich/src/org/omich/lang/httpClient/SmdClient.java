package org.omich.lang.httpClient;

import java.util.List;

import org.omich.lang.json.JSONAuthData;
import org.omich.lang.json.JSONData;
import org.omich.lang.words.Language;

import android.util.Log;

import com.ccg.util.JavaString;

public class SmdClient {
	
	private HttpRequest request;
	
	private long lastConnect = 0;
	
	private String login;
	private String password;
	
	public SmdClient(){
		request = new HttpRequest();
	}
	
	public boolean auth(String login, String password) throws Exception {
		
		this.login = login;
		this.password = password;
		
		return _auth(login, password);
	}
	
	public List<Language> getWords() throws Exception{
		
		List<Language> languages = null;
		
		if(_auth(login, password)){
			
			String response = request.getWords();
			response = JavaString.decode(response);
			Log.d("test", response);
			JSONData jData = new JSONData(response);
			languages = jData.getLanguage();
		}
		
		return languages;
	}
	
	public List<Language> Sync(List<Language> languages, long lastconnect) throws Exception{
			
		JSONData jData = new JSONData();
		jData.put(languages, lastconnect);
		String sendingData = jData.toString();
		sendingData = JavaString.encode(sendingData);
			
		String newWords = request.addWords(sendingData);
		newWords = JavaString.decode(newWords);
			
		jData = new JSONData(newWords);
		lastConnect = jData.getLastConnect();
			
		return jData.getLanguage();
	}
	
	public long getLastConnect(){
		return lastConnect;
	}
	
	private boolean _auth(String login, String password) throws Exception{
		
		String result = request.auth(login, password); 
		result = JavaString.decode(result);
		JSONAuthData jAuthData = new JSONAuthData(result);
		
		return jAuthData.getSuccess();
	}
}
