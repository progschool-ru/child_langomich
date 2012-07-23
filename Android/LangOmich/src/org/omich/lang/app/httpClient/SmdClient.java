package org.omich.lang.app.httpClient;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.json.JSONAuthData;
import org.omich.lang.app.json.JSONData;
import org.omich.lang.words.Language;

import android.util.Log;

import com.ccg.util.JavaString;

public class SmdClient {
	
	private HttpRequest request;
	
	public SmdClient()
	{
		request = new HttpRequest();
	}
	public String getCookie()
	{
		if(request.getCookie()!= null)
			return request.getCookie().getValue();
		else
			return null;
	}
	public void setCookie(String value)
	{
		Header h = new BasicHeader("Cookie",value);
		request.setCookie(h);
	}	
	public boolean auth(String login, String password) throws Exception 
	{				
		return _auth(login, password);
	}
	public boolean isLoggedIn() throws Exception
	{		
		String result = request.isLoggedIn(); 
		result = JavaString.decode(result);
		JSONObject jObject = new JSONObject(result);
		return jObject.getBoolean("isLoggedIn");		
	}	
	public String timing(List<Word> words, List<Dict> dicts, long serverTime) throws Exception
	{
		JSONData jData = new JSONData();
		jData.put(words, dicts, serverTime);
		String sendingData = jData.toString();
		System.out.println(sendingData);
		sendingData = JavaString.encode(sendingData);
		return request.addWords(sendingData);
	}		
	private boolean _auth(String login, String password) throws Exception{
		
		String result = request.auth(login, password); 
		result = JavaString.decode(result);
		JSONAuthData jAuthData = new JSONAuthData(result);
		
		return jAuthData.getSuccess();
	}
}
