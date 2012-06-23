package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.AppActivity;
import org.omich.lang.apptool.log.Log;

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
		Log.d("Hello!");
	}
}
