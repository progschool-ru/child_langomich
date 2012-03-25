package org.omich.lang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class PasswordDialogActivity  extends Activity implements OnClickListener{
	
	private EditText passwordEdit;
	
	private CheckBox show;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		
		View button_ok = findViewById(R.id.password_ok);
		button_ok.setOnClickListener(this);
		View button_cancel = findViewById(R.id.password_cancel);
		button_cancel.setOnClickListener(this);
		show = (CheckBox) findViewById(R.id.show);
		show.setOnClickListener(this);
		
		
		passwordEdit = (EditText) findViewById(R.id.passwordEdit); 
	
		String password = getIntent().getStringExtra(LangOmichSettings.PASSWORD);
		
		if(!password.equals(LangOmichSettings.DEFAULT_PASSWORD)){
			passwordEdit.setText(password);
		}
		
		boolean isChecked = getIntent().getBooleanExtra(LangOmichSettings.SHOW, LangOmichSettings.DEFAULT_SHOW);
		setInputType(isChecked);
		show.setChecked(isChecked);
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	public void onClick(View v){
		
		switch(v.getId()){
			
		case R.id.password_cancel:
				finish();
				break;
		case R.id.password_ok:
				Intent data = new Intent();
				String password = passwordEdit.getText().toString();
				if(password.equals("")) password = LangOmichSettings.DEFAULT_PASSWORD;
				data.putExtra(LangOmichSettings.PASSWORD, password);
				data.putExtra(LangOmichSettings.SHOW, show.isChecked());
				setResult(RESULT_OK, data );
				finish();
				break;
		case R.id.show:
				setInputType(show.isChecked());
			break;
			
		}
	}
	
	private void setInputType(boolean chechked){
		if(chechked){
			passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			
			passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
	}
}
