package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.AppActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class NewAccountActivity extends AppActivity
{
	SharedPreferences sp;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_newaccount);
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		((EditText)findViewById(R.id.newaccount_loginEdit)).setText(sp.getString("login", ""));
		((EditText)findViewById(R.id.newaccount_passwordEdit)).setText(sp.getString("password", ""));
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	//==== events =============================================================
	public void onNewAccountButtonOk (View v)
	{		
		String login = ((EditText)findViewById(R.id.newaccount_loginEdit)).getText().toString();
		String password = ((EditText)findViewById(R.id.newaccount_passwordEdit)).getText().toString();	
		login = login.trim();
		password = password.trim();
	    Editor ed = sp.edit();
	    ed.putString("login", login);
	    ed.putString("password", password);
	    ed.commit();
	    Intent intent = new Intent();
	    intent.putExtra("isNewAccount", true);
	    setResult(RESULT_OK, intent);	    
		finish();
	}
	public void onNewAccountButtonCancel (View v)
	{
	    Intent intent = new Intent();
	    intent.putExtra("isNewAccount", false);
	    setResult(RESULT_OK, intent);			
		finish();
	}	
}

