package org.omich.lang;

import java.util.List;


import org.omich.lang.SQLite.WordsData;
import org.omich.lang.words.Word;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DictionaryActivity extends LangOmichActivity{
	
	
	ListView myListView;
	ListAdapter myAdapter;
	WordsData myData;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		
		addSettings();
		myListView = (ListView) findViewById(R.id.listView1);
		myData = new WordsData(this, lSettigs.getLanguageId());
		myData.open();
		List<Word> myList = myData.getAllWords();
		myData.close();
		
		myAdapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_1, myList);
		myListView.setAdapter(myAdapter);
	}

}
