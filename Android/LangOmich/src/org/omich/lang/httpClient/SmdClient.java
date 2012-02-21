package org.omich.lang.httpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.omich.lang.Constants;

public class SmdClient {
	private HttpClient httpClient;
	
	public SmdClient(){
		httpClient = new DefaultHttpClient();
	}
	
	public String auth(String login, String password) throws Exception{
		
		HttpPost postRequest = new HttpPost("http://lang.omich.net/smdserver/servlet/login");
		
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		postParameters.add(new BasicNameValuePair(Constants.STR_LOGIN, login));
		postParameters.add(new BasicNameValuePair(Constants.STR_PASSWORD, password));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
		
		postRequest.setEntity(entity);
		
		HttpResponse response = httpClient.execute(postRequest);
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		return inputSreamToString(response.getEntity().getContent());
	}
	
	public String getWords()throws Exception{
		
		HttpPost postRequest = new HttpPost("http://lang.omich.net/smdserver/servlet/getWords");
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		return inputSreamToString(response.getEntity().getContent());
	}
	
	public String addWords(String data) throws ClientProtocolException, IOException{
		
		HttpPost postRequest = new HttpPost("http://lang.omich.net/smdserver/servlet/addWords");
		
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
