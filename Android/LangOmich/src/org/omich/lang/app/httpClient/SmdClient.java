package org.omich.lang.app.httpClient;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IWStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.json.JSONAuthData;
import org.omich.lang.app.json.JSONConverter;

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
		String result = request.auth(login, password); 
		result = JavaString.decode(result);
		JSONAuthData jAuthData = new JSONAuthData(result);
		
		return jAuthData.getSuccess();
	}	
	public boolean isLoggedIn() throws Exception
	{		
		String result = request.isLoggedIn(); 
		result = JavaString.decode(result);
		JSONObject jObject = new JSONObject(result);
		return jObject.getBoolean("isLoggedIn");		
	}	
	public long timing(List<Word> words, List<Dict> dicts, long serverTime, IWStorage mDbW) throws Exception
	{
		JSONConverter jConverter = new JSONConverter();
		String sendingData = jConverter.toString(words, dicts, serverTime);
		System.out.println(sendingData);
		sendingData = JavaString.encode(sendingData);
		String result = request.addWords(sendingData);
		result = JavaString.decode(result);
		System.out.println(result);
		return jConverter.write(result, mDbW);
	}		
}
