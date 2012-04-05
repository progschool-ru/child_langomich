package org.omich.lang;
import java.util.Date;
import java.util.zip.DataFormatException;

import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsData;
import org.omich.lang.words.Word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebStorage.Origin;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


public class EditWordActivity extends Activity implements OnClickListener{
	
	
	EditText editOriginal;
	EditText editTranslate;
	Spinner editRating;
	Button cancelButton;
	Button saveButton;
	
	int language_id;
	
	Word editable;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_word);
		
		Intent intent = getIntent();
		
	
		
		long word_id = intent.getLongExtra(MySQLiteHelper.WORD_ID, -1);
		String word_original = intent.getStringExtra(MySQLiteHelper.ORIGINAL);
		String word_translate = intent.getStringExtra(MySQLiteHelper.TRANSLATION);
		int word_rating  =  intent.getIntExtra(MySQLiteHelper.RATING, -1);
		language_id = intent.getIntExtra("lang_id", -1);
		editable = new Word(word_original, word_translate, word_rating, (new Date()).getTime());
		editable.setId(word_id);
		
		editOriginal = (EditText) findViewById(R.id.editOriginal);
		editTranslate = (EditText) findViewById(R.id.editTranslate);
		editRating = (Spinner) findViewById(R.id.editRating);
		
		Integer[] array = {0,1,2,3,4,5,6,7,8,9,10};
		ArrayAdapter<Integer> ratingAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, array);
		editRating.setAdapter(ratingAdapter);
		
		editRating.setSelection(editable.getRating());
		
		cancelButton = (Button) findViewById(R.id.cancel);
		cancelButton.setOnClickListener(this);
		saveButton = (Button) findViewById(R.id.ok);
		saveButton.setOnClickListener(this);
		
		editOriginal.setText(editable.getOriginal());
		editTranslate.setText(editable.getTranslation());
		
	}
	
	public void onClick(View v){
		switch(v.getId()){
			case R.id.cancel:
				finish();
				break;
			case R.id.ok:
				String original = editOriginal.getText().toString();
				String translate = editTranslate.getText().toString();
				int rating = editRating.getSelectedItemPosition();
				
				if(!original.equals(editable.getOriginal()) ||
						!translate.equals(editable.getTranslation()) ||
						rating != editable.getRating()){
					
					editable.setOriginal(original);
					editable.setTranslation(translate);
					editable.setRating(rating);
					
					WordsData myData = new WordsData(this, language_id);
					myData.open();
					myData.update(editable);
					myData.close();
					
					finish();
				}
				
				break;
		}
	}
}
