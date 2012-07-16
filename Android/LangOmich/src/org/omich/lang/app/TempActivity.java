package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.app.words.AddWordActivity;
import org.omich.lang.app.words.GameActivity;
import org.omich.lang.app.words.WordsListActivity;
import org.omich.lang.apptool.activity.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TempActivity extends AppActivity
{
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_temp);
	}
	//==== events =============================================================
	public void onAddWord (View v)
	{
		getForResultStarter().startForResult(new Intent(this, AddWordActivity.class), null);
	}
	
	public void onShowWords (View v)
	{
		getForResultStarter().startForResult(new Intent(this, WordsListActivity.class), null);
	}
	public void onGame (View v)
	{
		getForResultStarter().startForResult(new Intent(this, GameActivity.class), null);
	}
}
