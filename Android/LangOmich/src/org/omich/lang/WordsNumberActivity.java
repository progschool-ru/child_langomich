package org.omich.lang;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WordsNumberActivity extends ListActivity{

	String[] numbers = {"1","2","3","4","5","6"};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers);
		setListAdapter(myAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String select  = (String) getListAdapter().getItem(position);
		Intent data = new Intent();
		data.putExtra(LangOmichSettings.NUMBER_WORDS, select);
		setResult(RESULT_OK, data);
	    finish();
	}

}
