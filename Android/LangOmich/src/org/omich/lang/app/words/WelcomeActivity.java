package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.AppActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppActivity
{
	final int REQUEST_CODE_ADD_WORD = 200;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_welcome);
	}
	public void onStartButton (View v)
	{
		startActivityForResult(new Intent(this, FirstAddWordActivity.class), REQUEST_CODE_ADD_WORD);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_WORD )
		{
			startActivity(new Intent(this, WordsListActivity.class));
			finish();
		}
	}		
}
