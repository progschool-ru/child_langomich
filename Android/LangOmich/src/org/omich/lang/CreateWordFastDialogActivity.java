package org.omich.lang;

import java.util.Date;

import org.omich.lang.SQLite.IQueryLanguage;
import org.omich.lang.SQLite.IQueryWords;
import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsStorage;
import org.omich.lang.words.Word;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateWordFastDialogActivity extends Activity implements OnClickListener{
	
	EditText editOriginal;
	EditText editTranslation;
	Spinner selectRating;
	TextView errorMessage;
	
	Button create;
	Button cancel;
	
	
	int languageId;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_word_fast);
		
		languageId = getIntent().getExtras().getInt(MySQLiteHelper.LANGUAGE_ID);
		
		editOriginal = (EditText) findViewById(R.id.createWf_tEdit_original);
		editTranslation = (EditText) findViewById(R.id.createWf_tEdit_translation);
		selectRating = (Spinner) findViewById(R.id.createWf_spinner_rating);
		errorMessage = (TextView) findViewById(R.id.createWf_tView_error_report);
		create = (Button) findViewById(R.id.createWf_button_create);
		cancel = (Button) findViewById(R.id.createWf_button_cancel);
		
		errorMessage.setTextColor(Color.RED);
		create.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		Integer[] a_ratings = {0,1,2,3,4,5,6,7,8,9};
		ArrayAdapter<Integer> ratings_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, a_ratings);
		selectRating.setAdapter(ratings_adapter);
	}

	public void onClick(View v) {
			switch(v.getId()){
				
			case R.id.createWf_button_create:
				safe();
				break;
			case R.id.createWf_button_cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
		
	}
	
	private void safe(){
		WordsStorage wordsStorage = new WordsStorage(this);
		wordsStorage.open();
		IQueryLanguage queryLanguage = wordsStorage.getQueryLanguage(languageId);
		IQueryWords queryWords = queryLanguage.getQueryWords();
		
		boolean isReady = true;
		
		String original = editOriginal.getText().toString().trim();
		
		if(original.isEmpty()){
			errorMessage.append(getText(R.string.error_empty_original));
			errorMessage.append("\n");
			isReady = false;
		}
			
		String translation = editTranslation.getText().toString().trim();
			
		if(translation.isEmpty()){
			errorMessage.append(getText(R.string.error_empty_translation));
			isReady = false;
		}
		
		
		if(isReady){
			int rating  = (Integer) selectRating.getSelectedItem();
			Word word = new Word(-1,original,translation, rating, new Date().getTime(), 0);
			long id = queryWords.createWord(word);
			if(id  == -1){
				errorMessage.append(getText(R.string.error_in_base));
			}else{
				Intent result = new Intent();
				
				result.putExtra(MySQLiteHelper.WORD_ID, id);
				result.putExtra(MySQLiteHelper.ORIGINAL, word.getOriginal());
				result.putExtra(MySQLiteHelper.TRANSLATION, word.getTranslation());
				result.putExtra(MySQLiteHelper.RATING, word.getRating());
				result.putExtra(MySQLiteHelper.MODIFIED, word.getModified());
				result.putExtra(MySQLiteHelper.WORD_IN_SERVER, word.getInServer());
				
				setResult(RESULT_OK, result);
				finish();
			}
			
		}
		
		wordsStorage.close();
		return;
	}

}
