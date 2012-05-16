package org.omich.lang;
import java.util.Date;

import org.omich.lang.SQLite.IQueryLanguage;
import org.omich.lang.SQLite.IQueryWords;
import org.omich.lang.SQLite.IWordsStorage;
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


public class EditWordActivity extends Activity implements OnClickListener{
	
	
	EditText editOriginal;
	EditText editTranslate;
	Spinner editRating;
	Button cancelButton;
	Button saveButton;
	
	TextView error_report;
	int language_id;
	
	Word editable;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_word);
		
		Intent intent = getIntent();
		
	
		
		long id = intent.getLongExtra(MySQLiteHelper.WORD_ID, -1);	
		String original = intent.getStringExtra(MySQLiteHelper.ORIGINAL);
		String translation = intent.getStringExtra(MySQLiteHelper.TRANSLATION);
		int rating = intent.getIntExtra(MySQLiteHelper.RATING, 0);
		long modified = intent.getLongExtra(MySQLiteHelper.MODIFIED, 0);
		boolean in_server = intent.getBooleanExtra(MySQLiteHelper.WORD_IN_SERVER, false);
		
		int inServer = 0;
		if(in_server) inServer = -1;
		
		editable = new Word(id, original, translation, rating, modified, inServer);
		
		language_id = intent.getIntExtra(MySQLiteHelper.LANGUAGE_ID, -1);
		
	
		
		editOriginal = (EditText) findViewById(R.id.editW_tEdit_original);
		editTranslate = (EditText) findViewById(R.id.editW_tEdit_translation);
		editRating = (Spinner) findViewById(R.id.editW_spinner_rating);
		
		error_report = (TextView) findViewById(R.id.editW_tView_error_report);
		error_report.setTextColor(Color.RED);
		
		Integer[] ratings = {0,1,2,3,4,5,6,7,8,9};
		ArrayAdapter<Integer> ratingAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, ratings);
		editRating.setAdapter(ratingAdapter);
		
		editRating.setSelection(editable.getRating());
		
		cancelButton = (Button) findViewById(R.id.editW_button_cancel);
		cancelButton.setOnClickListener(this);
		saveButton = (Button) findViewById(R.id.editW_button_save);
		saveButton.setOnClickListener(this);
		
		editOriginal.setText(editable.getOriginal());
		editTranslate.setText(editable.getTranslation());
		
	}
	
	public void onClick(View v){
		
		
		switch(v.getId()){
			case R.id.editW_button_cancel:
				
				setResult(RESULT_CANCELED);
				finish();
				
				break;
			case R.id.editW_button_save:
				
				String original = editOriginal.getText().toString().trim();
				String translate = editTranslate.getText().toString().trim();
				int rating = editRating.getSelectedItemPosition();
				
				editable.setModified(new Date().getTime());
				
				boolean isEmpty = false;
				
				error_report.setText(null);
				
				if(original.isEmpty()){
					error_report.append(getString(R.string.error_empty_original));
					error_report.append("\n");
					isEmpty = true;
				}
				
				if(translate.isEmpty()){
					error_report.append(getString(R.string.error_empty_translation));
					error_report.append("\n");
					isEmpty = true;
				}
				
				IWordsStorage wordsStorage = new WordsStorage(this);
				wordsStorage.open();
				IQueryLanguage queryLanguage = wordsStorage.getQueryLanguage(language_id);
				IQueryWords queryWords = queryLanguage.getQueryWords();
				
				if(!isEmpty){
					
					boolean resetWord = original.equals(editable.getOriginal());
					boolean isChange = false;
					if(!translate.equals(editable.getTranslation())) isChange = true;
					if( editable.getRating() != rating ) isChange = true;
					
					
					if(resetWord){
						if(isChange){
							editable.setTranslation(translate);
							editable.setRating(rating);
							queryWords.updateWord(editable, IQueryWords.FIND_BY_ID);
						}else{
							error_report.setText(getText(R.string.editW_nothing_change));
							return;
						}
					}else{
						editable.setOriginal(original);
						editable.setTranslation(translate);
						editable.setRating(rating);
						
						long id = queryWords.updateWord(editable, IQueryWords.UPDATE_BY_ORIGINAL);
							if(id == -1){
								error_report.setText(getText(R.string.error_in_base));
							return;
							}else{
								editable.setId(id);
							}
					}
					
				}else{
					return;
				}
				wordsStorage.close();
				Intent result = new Intent();
				 
				result.putExtra(MySQLiteHelper.WORD_ID, editable.getId());
				result.putExtra(MySQLiteHelper.ORIGINAL, editable.getOriginal());
				result.putExtra(MySQLiteHelper.TRANSLATION, editable.getTranslation());
				result.putExtra(MySQLiteHelper.RATING, editable.getRating());
				result.putExtra(MySQLiteHelper.MODIFIED, editable.getModified());
				result.putExtra(MySQLiteHelper.WORD_IN_SERVER, editable.getInServer());
				
				setResult(RESULT_OK, result);
				
				finish();
				
				break;
			}
				
				
		}
}

