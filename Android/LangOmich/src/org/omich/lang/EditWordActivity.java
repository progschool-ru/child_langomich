package org.omich.lang;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsData;
import org.omich.lang.json.JSONWords;
import org.omich.lang.words.Word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


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
		String data = intent.getStringExtra("data");
		
		try {
			editable = JSONWords.parseWord(new JSONObject(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		language_id = intent.getIntExtra("lang_id", -1);
		
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
				
				setResult(RESULT_CANCELED);
				finish();
				
				break;
			case R.id.ok:
				
				String original = editOriginal.getText().toString();
				String translate = editTranslate.getText().toString();
				int rating = editRating.getSelectedItemPosition();
				
				editable.setModified(new Date().getTime());
				
				boolean  isChange = false;
				
				boolean resetWord = original.equals(editable.getOriginal());
				
				if( rating != editable.getRating()) isChange = true;
				if(  !translate.equals(editable.getTranslation()) && !translate.equals("")) isChange = true;
				

				WordsData myData = new WordsData(this, language_id);
				
				myData.open();
				
				if( resetWord ){
					if(isChange){
						editable.setTranslation(translate);
						editable.setRating(rating);	
						myData.update(editable);
					}else{
						return;
					}
				}else{
					if(!original.equals("") && myData.originalIsFree(original)){
							myData.toDel(editable);
							editable.setOriginal(original);
							editable.setTranslation(translate);
							editable.setRating(rating);
								myData.createWord(editable);
						
					}else{
						return;
					}
				}
				
				myData.close();
					
				Intent result = new Intent();
				 
			
				try {
					String	newData = JSONWords.wordToJSON(editable).toString();
					result.putExtra("data", newData);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				setResult(RESULT_OK, result);
				
				finish();
				
				break;
			}
				
				
		}
}

