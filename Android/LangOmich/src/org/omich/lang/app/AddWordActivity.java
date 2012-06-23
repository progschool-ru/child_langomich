package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.AppActivity;
import org.omich.tool.log.Log;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class AddWordActivity extends AppActivity
{
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_addword);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	//==== events =============================================================
	public void onAddButton (View v)
	{
		Log.d("onAddButton");
	}
}
