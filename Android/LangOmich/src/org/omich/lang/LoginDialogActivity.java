package org.omich.lang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LoginDialogActivity extends Activity implements OnClickListener {

	private EditText loginEdit;
	 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	
		View button_ok = findViewById(R.id.login_ok);
		button_ok.setOnClickListener(this);
		View button_cancle = findViewById(R.id.login_cancel);
		button_cancle.setOnClickListener(this);
		
		loginEdit = (EditText) findViewById(R.id.loginEdit);
		
		String login = getIntent().getStringExtra(LangOmichSettings.LOGIN);
		
		
		if(!login.equals(LangOmichSettings.DEFAULT_LOGIN)){
			loginEdit.setText(login);
		}
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	public void onClick(View v){
		
		switch(v.getId()){
			case R.id.login_cancel:
				finish();
				break;
			case R.id.login_ok:
				Intent data = new Intent();
				String login = loginEdit.getText().toString();
				if(login.equals("")) login = LangOmichSettings.DEFAULT_LOGIN;
				data.putExtra(LangOmichSettings.LOGIN, login);
				setResult(RESULT_OK, data);
				finish();
				break;
		}
	}
}
