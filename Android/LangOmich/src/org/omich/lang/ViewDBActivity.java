package org.omich.lang;

import java.util.List;

import org.omich.lang.SQLite.IWordsStorage;
import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsStorage;
import org.omich.lang.words.Language;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewDBActivity extends Activity{
	
	public final int REQUEST_NEW_LANGUAGES = 0;
	
	Spinner selectLang;
	IWordsStorage wordsStorage;
	ArrayAdapter<Language> myAdapter;
	
	
	TextView id;
	TextView serverId;
	EditText name;
	
	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_db);
		
		selectLang = (Spinner) findViewById(R.id.select_lang);
		Button newLanguage = (Button) findViewById(R.id.new_language);
		
		wordsStorage = new WordsStorage(this);
		wordsStorage.open();
		
		
		
		List<Language> myList = wordsStorage.getLanguages();
		wordsStorage.close();
		myAdapter = new ArrayAdapter<Language>(this, android.R.layout.simple_spinner_item, myList);
		selectLang.setAdapter(myAdapter);
		
		
		
		selectLang.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				Language language = (Language) arg0.getItemAtPosition(arg2);
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		newLanguage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(), CreateLanguageDialogActivity.class);
				startActivityForResult(intent, REQUEST_NEW_LANGUAGES);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult( int requesCode, int resultCode, Intent data){
		
		if(resultCode == RESULT_OK){
			
			switch(requesCode){
				case  REQUEST_NEW_LANGUAGES:
					String name = data.getExtras().getString(MySQLiteHelper.NAME);
					int id = data.getExtras().getInt(MySQLiteHelper.LANGUAGE_ID);
					Language language = new Language(id, "", name, null);
					myAdapter.add(language);
					myAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
	
}