package org.omich.lang;

import java.util.List;


import org.omich.lang.SQLite.WordsData;
import org.omich.lang.comparators.ComparatorOriginalUp;
import org.omich.lang.comparators.ComparatorRatingDown;

import org.omich.lang.words.Word;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class DictionaryActivity extends LangOmichActivity implements OnClickListener{
	
	
	ListView myListView;
	MyAdapter myAdapter;
	WordsData myData;
	
	Button sortByOriginalButton;
	Button sortByTranslateButton;
	Button sortByRatingButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		
		addSettings();
		
		sortByOriginalButton = (Button) findViewById(R.id.sort_by_original);
		sortByOriginalButton.setOnClickListener(this);
		
		sortByRatingButton = (Button) findViewById(R.id.sort_by_rating);
		sortByRatingButton.setOnClickListener(this);
		
		myListView = (ListView) findViewById(R.id.listView1);
		myData = new WordsData(this, lSettigs.getLanguageId());
		myData.open();
		List<Word> myList = myData.getAllWords();
		myData.close();
		
		myAdapter = new MyAdapter(this, myList);
		myListView.setAdapter(myAdapter);
	}
	
	
	public void onClick(View v){
		switch(v.getId()){
			
			case R.id.sort_by_original:
				myAdapter.sort(new ComparatorOriginalUp());
				break;
			case R.id.sort_by_rating:
				myAdapter.sort(new ComparatorRatingDown());
				break;
		}
	}

}
