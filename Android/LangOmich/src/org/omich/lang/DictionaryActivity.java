package org.omich.lang;

import java.util.List;

import org.omich.lang.SQLite.LanguagesData;
import org.omich.lang.words.Language;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DictionaryActivity extends LangOmichActivity{
	
	
	ListView myListView;
	ListAdapter myAdapter;
	LanguagesData myData;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		
		myListView = (ListView) findViewById(R.id.listView1);
		myData = new LanguagesData(this);
		myData.open();
		List<Language> myList = myData.getListAllLanguages();
		myData.close();
		myAdapter = new ArrayAdapter<Language>(this, android.R.layout.simple_list_item_1, myList);
		myListView.setAdapter(myAdapter);
	}

}
