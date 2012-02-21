package org.omich.lang;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONLanguages;
import org.omich.lang.json.JSONParser;
import org.omich.lang.json.JSONWriter;
import org.omich.lang.words.Language;
import org.omich.lang.words.Word;

import com.ccg.util.JavaString;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


public class TestActivity extends Activity {
	
	//private WordsDataSource dataSorce;
	
	private TextView authResView; 
	private TextView dataView;
	private TextView newWordsView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_view);
		
		authResView = (TextView) findViewById(R.id.AuthResView);
		dataView = (TextView) findViewById(R.id.data);
		newWordsView = (TextView) findViewById(R.id.newWords);
		
		List<Language> languages = new ArrayList<Language>();
		List<Word> words = new ArrayList<Word>();
		Word word = new Word("имя", "name", 3);
		words.add(word);
		Language language = new Language("BR", null, words);
		languages.add(language);
		
		Intent intent = getIntent();
		
		String login = intent.getStringExtra(Constants.STR_LOGIN);
		String password = intent.getStringExtra(Constants.STR_PASSWORD);
		
		
		SmdClient smdClient = new SmdClient();
		String response;
		try {
			String authRes = JavaString.decode(smdClient.auth(login, password));
			
			if(JSONParser.parseAuth(authRes) == true){
				authResView.setText("successed");
			}else{
				authResView.setText("false");
			}
			
			String data = JSONWriter.toJSON(0, languages).toString();
			dataView.setText(data);
			response = JavaString.decode(smdClient.addWords(JavaString.encode(data)));
			long lastConnection = 0;
			lastConnection = JSONParser.getLastConnection(response);
			languages = JSONParser.parseLanguages(response);
			
			ListIterator<Language> iter = languages.listIterator();
			
			while(iter.hasNext()){
				newWordsView.append(iter.next().getName()+" ");
			}
			
			newWordsView.append(" "+lastConnection);
			
		}catch (Exception e) {
			authResView.setText(e.getMessage());
		}
	
	
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
		
}