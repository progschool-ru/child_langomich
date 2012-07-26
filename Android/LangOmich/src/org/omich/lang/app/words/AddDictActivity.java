package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class AddDictActivity extends BcActivity
{
	private String mAddDictTaskId;
	
	private boolean mIsDestroyed;
	private int size = 1;
	private SharedPreferences sp;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_adddict);
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		Intent intent = getIntent();
		size = intent.getIntExtra("dictsSize", 1);
	}
	
	@Override
	protected void onDestroy ()
	{
		if(mAddDictTaskId != null)
		{
			getBcConnector().unsubscribeTask(mAddDictTaskId);
			mAddDictTaskId = null;
		}
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//==== events =============================================================
	public void onCreateButton (View v)
	{
		if(mAddDictTaskId != null)
			return;

		String name = ((EditText)findViewById(R.id.adddict_dictNameEdit)).getText().toString();
		String taskAddText = getResources().getString(R.string.adddict_added);

		if(name.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.adddict_errorReport);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.adddict_emptyString);
		}
		else
		{
			Intent intent = AddDictTask.createIntent(name, taskAddText);
			mAddDictTaskId = getBcConnector().startTypicalTask(AddDictTask.class, intent, new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
		
						mAddDictTaskId = null;
						
						long dictId = bundle.getLong(BundleFields.DICT_ID);
						if(dictId == -1)
						{
							TextView errorView = (TextView) findViewById(R.id.adddict_errorReport);
							errorView.setTextColor(Color.RED);
							errorView.setText(R.string.adddict_inBase);														
						}
						else
						{
		        			Editor ed = sp.edit();
		        			ed.putInt(PreferenceFields.DICT_POSITION, size - 1);
		        			ed.putLong(PreferenceFields.DICT_ID, dictId);
		        			ed.commit();
							finish();
						}
					}
				});		
		}
	}
	public void onCancelButton (View v)
	{	
		finish();
	}	
}
