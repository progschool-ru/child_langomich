package org.omich.lang;

import org.omich.lang.SQLite.IWordsStorage;
import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsStorage;
import org.omich.lang.words.Language;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateLanguageDialogActivity extends Activity implements OnClickListener {

	Button cancel;
	Button ok;
	EditText name;
	TextView errorView;
	
	IWordsStorage wordsStorage;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_language);
		
		cancel = (Button) findViewById(R.id.createL_button_cancel);
		ok = (Button) findViewById(R.id.createL_button_create);
		name = (EditText) findViewById(R.id.createL_tEdit_language_name);
		errorView = (TextView) findViewById(R.id.createL_tView_error_report);
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
		
		errorView.setTextColor(Color.RED);
		
		wordsStorage = new WordsStorage(this);
	}

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.createL_button_cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;
			case R.id.createL_button_create:
				
				String s_name  = name.getText().toString();
				s_name = s_name.trim();
				
				if(!s_name.equals("")){
					
					wordsStorage.open();
					long id = wordsStorage.create(new Language(-1, null, s_name, null));
					wordsStorage.close();
					
					if(id != -1){
						Intent result = new Intent();
						result.putExtra(MySQLiteHelper.LANGUAGE_ID, (int)id);
						result.putExtra(MySQLiteHelper.NAME, s_name);
						setResult(RESULT_OK, result);
						finish();
					}else{
						errorView.setText(R.string.createL_in_base);
					}
				}else{
					errorView.setText(R.string.createL_empty_string);
				}
				break;
		}
	}
}
