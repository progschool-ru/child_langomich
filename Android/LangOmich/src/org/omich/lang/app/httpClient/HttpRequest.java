package org.omich.lang.app.httpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.omich.lang.LangOmichSettings;

public class HttpRequest {
	
	private static final String LOGIN = "http://lang.omich.net/smdserver/servlet/login";
	private static final String IS_LOGGED_IN  = "http://lang.omich.net/smdserver/servlet/isLoggedIn";
	private static final String GET_WORDS = "http://lang.omich.net/smdserver/servlet/getWords"; 
	private static final String ADD_WORDS = "http://lang.omich.net/smdserver/servlet/addWords";
	private HttpClient httpClient;
	
	public HttpRequest(){
		httpClient = new DefaultHttpClient();
	}
	
	public String auth(String login, String password) throws Exception{
		
		HttpPost postRequest = new HttpPost(LOGIN);
		
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		postParameters.add(new BasicNameValuePair(LangOmichSettings.LOGIN, login));
		postParameters.add(new BasicNameValuePair(LangOmichSettings.PASSWORD, password));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
		
		postRequest.setEntity(entity);
		
		HttpResponse response = httpClient.execute(postRequest);
				
		return inputSreamToString(response.getEntity().getContent());
	}
	public String isLoggedIn() throws Exception
	{
		
		HttpPost postRequest = new HttpPost(IS_LOGGED_IN);
		
		HttpResponse response = httpClient.execute(postRequest);
				
		return inputSreamToString(response.getEntity().getContent());
	}	
	public String getWords()throws Exception{
		
		HttpPost postRequest = new HttpPost(GET_WORDS);
		HttpResponse response = httpClient.execute(postRequest);
		
		return inputSreamToString(response.getEntity().getContent());
	}
	
	public String addWords(String data) throws ClientProtocolException, IOException{
		
		HttpPost postRequest = new HttpPost(ADD_WORDS);
		
		List<NameValuePair> postParametrs = new ArrayList<NameValuePair>();
		
		postParametrs.add(new BasicNameValuePair("data", data));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParametrs);
		
		postRequest.setEntity(entity);
		
		HttpResponse response = httpClient.execute(postRequest);
		
		return inputSreamToString(response.getEntity().getContent());
		
	}
	
	private String inputSreamToString(InputStream input) throws IOException{
		
		StringBuilder builder = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(input));
		
		String line;
		while((line = rd.readLine()) != null )
			builder.append(line);
		
		return builder.toString();
	}
}
