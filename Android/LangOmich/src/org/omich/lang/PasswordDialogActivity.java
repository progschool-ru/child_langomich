package org.omich.lang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PasswordDialogActivity  extends Activity implements OnClickListener{
	
	public static final String PASSWORD = "password";
	
	
	private EditText passwordEdit;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		
		View button_ok = findViewById(R.id.password_ok);
		button_ok.setOnClickListener(this);
		View button_cancel = findViewById(R.id.password_cancel);
		button_cancel.setOnClickListener(this);
		
		passwordEdit = (EditText) findViewById(R.id.passwordEdit); 
	
		String password = getIntent().getStringExtra(PASSWORD);
		passwordEdit.setText(password);
		
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
				data.putExtra(PASSWORD, password);
				setResult(RESULT_OK, data );
				finish();
				break;
		}
	}
}
