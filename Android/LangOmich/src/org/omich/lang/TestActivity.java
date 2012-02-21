package org.omich.lang;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.omich.lang.json.JSONLanguages;
import org.omich.lang.json.JSONWriter;
import org.omich.lang.words.Language;
import org.omich.lang.words.Word;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


public class TestActivity extends Activity {
	
	//private WordsDataSource dataSorce;
    
	private ListView listView;
	
	private TextView textView; 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_view);
		
		textView = (TextView) findViewById(R.id.textView1);
		
		List<Language> languages = new ArrayList<Language>();
		List<Word> words = new ArrayList<Word>();
		Word word = new Word("имя", "name", 3);
		words.add(word);
		Language language = new Language("EN", "12323123", words);
		languages.add(language);
		
		
		try {
			textView.setText(JSONWriter.toJSON(0, languages));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
		
}