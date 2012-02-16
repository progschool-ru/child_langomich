package org.omich.lang.httpClient;

import java.io.BufferedReader;
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
import org.omich.lang.Constants;

public class SmdClient {
	private HttpClient httpClient;
	
	public SmdClient(){
		httpClient = new DefaultHttpClient();
	}
	
	public String auth(String login, String password) throws Exception{
		StringBuilder builder = new StringBuilder();
		HttpPost postRequest = new HttpPost("http://lang.omich.net/smdserver/servlet/login");
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(Constants.STR_LOGIN, login));
		postParameters.add(new BasicNameValuePair(Constants.STR_PASSWORD, password));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
		postRequest.setEntity(entity);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		while((line = rd.readLine()) != null)
				builder.append(line);
		
		return builder.toString();
	}
	
	public String getWords()throws Exception{
		StringBuilder builder = new StringBuilder();
		HttpPost postRequest = new HttpPost("http://lang.omich.net/smdserver/servlet/getWords");
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		while((line = rd.readLine()) != null )
			builder.append(line);
		
		return builder.toString();
	}
}
