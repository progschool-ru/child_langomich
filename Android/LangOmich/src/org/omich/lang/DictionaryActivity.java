package org.omich.lang;

import java.util.ArrayList;
import java.util.List;


import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsData;
import org.omich.lang.comparators.ComparatorOriginalDown;
import org.omich.lang.comparators.ComparatorOriginalUp;
import org.omich.lang.comparators.ComparatorRatingDown;
import org.omich.lang.comparators.ComparatorRatingUp;
import org.omich.lang.comparators.comparatorTranslateDown;
import org.omich.lang.comparators.comparatorTranslateUp;

import org.omich.lang.words.Word;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DictionaryActivity extends LangOmichActivity implements OnClickListener{
	
	
	ListView myListView;
	MyAdapter myAdapter;
	WordsData myData;
	
	Button sortByOriginalButton;
	int sortStateOriginal = 0;
	Button sortByTranslateButton;
	int sortStateTranslate = 0;
	Button sortByRatingButton;
	int sortStateRating = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		
		addSettings();
		
		sortByOriginalButton = (Button) findViewById(R.id.sort_by_original);
		sortByOriginalButton.setOnClickListener(this);
		
		sortByTranslateButton = (Button) findViewById(R.id.sort_by_translate);
		sortByTranslateButton.setOnClickListener(this);
		
		sortByRatingButton = (Button) findViewById(R.id.sort_by_rating);
		sortByRatingButton.setOnClickListener(this);
		
		myListView = (ListView) findViewById(R.id.listView1);
		myData = new WordsData(this, lSettigs.getLanguageId());
		
		myListView.setOnItemLongClickListener(new OnWordClickListener());
	}
	
	@Override
	public void onResume(){
		super.onResume();
		myData.open();
		List<Word> myList =  myData.getAllWords();
		myData.close();
		myAdapter = new MyAdapter(this, myList);
		myListView.setAdapter(myAdapter);
		
	}
	
	public void onClick(View v){
		switch(v.getId()){
			
			case R.id.sort_by_original:
				
				if(sortStateOriginal == 0 || sortStateOriginal == 2){
				     myAdapter.sort(new ComparatorOriginalDown());
				     sortStateOriginal = 1;
				}else{
					 myAdapter.sort(new ComparatorOriginalUp());
					sortStateOriginal = 2;
				}
				break;
				
			case R.id.sort_by_translate:
				if(sortStateTranslate == 0 || sortStateTranslate == 2){
					  myAdapter.sort(new comparatorTranslateDown());
					sortStateTranslate = 1;
				}else{
					  myAdapter.sort(new comparatorTranslateUp());
					sortStateTranslate = 2;
				}
				break;
				
			case R.id.sort_by_rating:
				
				if(sortStateRating == 0 || sortStateRating == 2){
					  myAdapter.sort(new ComparatorRatingDown());
					sortStateRating = 1;
				}else{
					  myAdapter.sort(new ComparatorRatingUp());
					sortStateRating = 2;
				}
				
				break;
		}
	}
	
	private class OnWordClickListener implements OnItemLongClickListener{

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
			Word word = myAdapter.getItem(position);
				Intent intent = new Intent(getApplicationContext(), EditWordActivity.class);
				intent.putExtra("lang_id",lSettigs.getLanguageId());
				intent.putExtra(MySQLiteHelper.WORD_ID, word.getId());
				intent.putExtra(MySQLiteHelper.ORIGINAL, word.getOriginal());
				intent.putExtra(MySQLiteHelper.TRANSLATION, word.getTranslation());
				intent.putExtra(MySQLiteHelper.RATING, word.getRating() );
				startActivity(intent);
			return false;
		}	
	}
}
