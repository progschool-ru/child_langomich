package org.omich.lang;

import java.util.Date;

import org.omich.lang.SQLite.IQueryLanguage;
import org.omich.lang.SQLite.IQueryWords;
import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.IWordsStorage;
import org.omich.lang.SQLite.WordsStorage;
import org.omich.lang.words.Language;
import org.omich.lang.words.Word;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;



public class CreateWordActivity extends Activity implements OnClickListener{
	
	private final int REQUEST_NEW_LANGUAGES = 0;
	
	Spinner selectLanguage;
	ImageButton newLanguage;
	
	ArrayAdapter<Language> languageAdapter;
	IWordsStorage wordsStorage;
	
	IQueryLanguage currentLanguage;
	IQueryWords currentWords;
	
	TextView errorMessage;
	
	EditText editOriginal;
	EditText editTtranslation;
	
	Spinner selectRating;
	ArrayAdapter<Integer> ratingAdapter;
	
	Button create;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_word);
		
		selectLanguage = (Spinner) findViewById(R.id.createW_spinner_selectLanguage);
		newLanguage = (ImageButton) findViewById(R.id.createW_ibutton_createLanguage);
		
		editOriginal = (EditText) findViewById(R.id.createW_tEdit_original);
		editTtranslation = (EditText) findViewById(R.id.createW_tEdit_translation);
		selectRating = (Spinner) findViewById(R.id.createW_spinner_selectRating);
		errorMessage = (TextView) findViewById(R.id.createW_tView_errorMessage);
		create = (Button) findViewById(R.id.createW_button_create);
		
		create.setOnClickListener(this);
		newLanguage.setOnClickListener(this);
		
		wordsStorage = new WordsStorage(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	
		wordsStorage.open();
		languageAdapter = new ArrayAdapter<Language>(this, android.R.layout.simple_spinner_item, wordsStorage.getLanguages());
		selectLanguage.setAdapter(languageAdapter);
		selectLanguage.setOnItemSelectedListener(onLanguageSelect);
		
		Integer[] ratings = {0,1,2,3,4,5,6,7,8,9};
		ratingAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, ratings);
		selectRating.setAdapter(ratingAdapter);
		
		int count = languageAdapter.getCount();
		if(count == 0) setEnabel(false);
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		wordsStorage.close();
	}

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.createW_ibutton_createLanguage:
				Intent intent = new Intent(getApplicationContext(), CreateLanguageDialogActivity.class);
				startActivityForResult(intent, REQUEST_NEW_LANGUAGES);
				break;
			case R.id.createW_button_create:
				
				currentWords = currentLanguage.getQueryWords();		
				
				errorMessage.setText("");
				errorMessage.setTextColor(Color.RED);
				
				boolean isReady = true;
				
				String original = editOriginal.getText().toString().trim();
				
				if(original.isEmpty()){
					errorMessage.append(getText(R.string.error_empty_original));
					errorMessage.append("\n");
					isReady = false;
				}
					
				String translation = editTtranslation.getText().toString().trim();
					
				if(translation.isEmpty()){
					errorMessage.append(getText(R.string.error_empty_translation));
					isReady = false;
				}
					
					
				if(isReady){
					int rating  = (Integer) selectRating.getSelectedItem();
					IQueryWords wordsStorage = currentLanguage.getQueryWords();
					Word word = new Word(-1,original,translation, rating, new Date().getTime(), 0);
					if(wordsStorage.createWord(word) == -1){
						errorMessage.append(getText(R.string.error_in_base));
					}else{
						errorMessage.setTextColor(Color.GREEN);
						errorMessage.setText(R.string.createW_added_ok);
					}
				}
					
				break;
		}
		
	}

	@Override
	protected void onActivityResult(int requesCode, int resultCode, Intent data){
		
		if(resultCode == RESULT_OK){
			
			switch(requesCode){
				case REQUEST_NEW_LANGUAGES:
					String name = data.getExtras().getString(MySQLiteHelper.NAME);
					int id = data.getExtras().getInt(MySQLiteHelper.LANGUAGE_ID);
					Language language = new Language(id, null, name, null);
					int count = languageAdapter.getCount();
					if(count == 0) setEnabel(true);
					languageAdapter.add(language);
					languageAdapter.notifyDataSetChanged();
					break;
			}
		}
	}
	
	
	private OnItemSelectedListener onLanguageSelect = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Language language = (Language) arg0.getItemAtPosition(arg2);
			setCurrentData(language);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			currentLanguage = null;
			currentWords = null;
		}
	};
	
	private void setCurrentData(Language language){
		currentLanguage = wordsStorage.getQueryLanguage(language.getId());
	}
	
	private void setEnabel(boolean enabled){
		editOriginal.setEnabled(enabled);
		editTtranslation.setEnabled(enabled);
		selectRating.setEnabled(enabled);
	}
}