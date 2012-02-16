package org.omich.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SmdClient {
	
	private static final String URL = "http://lang.omich.net";
	private static final String LOGIN_API = "/smdserver/servlet/login";
	private static final String GET_WORDS_API = "smdserver/servlet/getWords";
	
	private HttpClient httpClient;
	
	public SmdClient(){
		httpClient = new DefaultHttpClient();
	}
	
	public String auth(String login, String password) throws Exception{
		HttpPost postRequest = new HttpPost(URL+LOGIN_API);
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(Constants.STR_LOGIN, login));
		postParameters.add(new BasicNameValuePair(Constants.STR_PASSWORD, password));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
		postRequest.setEntity(entity);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		return stremToString(rd);
	}
	
	public String getWords()throws Exception{
		HttpPost postRequest = new HttpPost(URL+GET_WORDS_API);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		return stremToString(rd);
	}
	
	private String stremToString(BufferedReader rd) throws IOException{
		StringBuilder bulder = new StringBuilder();
		String line ="";
		while((line = rd.readLine()) != null){
			bulder.append(line);
		}
		
		return rd.toString();
	}
}
