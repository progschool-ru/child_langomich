package org.omich.lang;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.SQLite.WordsDataSource;
import org.omich.lang.words.Word;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TestActivity extends Activity {
	
	private WordsDataSource dataSorce;
    
	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_view);
		List<Word> words = new  ArrayList<Word>();
		int layoutID = android.R.layout.simple_list_item_1;
		listView = (ListView) findViewById(R.id.listView1);
		dataSorce = new WordsDataSource(this);
		dataSorce.open();
		words = dataSorce.getAllWords();
		dataSorce.close();
		ArrayAdapter<Word> adapter = new ArrayAdapter<Word>(this, layoutID, words);
		listView.setAdapter(adapter);
	
		try{
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
		
		
	
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
		
}