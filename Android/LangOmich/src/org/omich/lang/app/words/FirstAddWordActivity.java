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
import android.widget.EditText;
import android.widget.TextView;

public class FirstAddWordActivity extends BcActivity
{
	private String mAddWordTaskId;
	
	private boolean mIsDestroyed;
	protected SharedPreferences sp;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_first_add_word);

		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		((EditText)findViewById(R.id.first_addword_dictEdit)).setText(getResources().getString(R.string.first_addword_text_exemple_dict));
		((EditText)findViewById(R.id.first_addword_nativEdit)).setText(getResources().getString(R.string.first_addword_text_exemple_nativ));
		((EditText)findViewById(R.id.first_addword_foreignEdit)).setText(getResources().getString(R.string.first_addword_text_exemple_foreign));
	}
	
	@Override
	protected void onDestroy ()
	{
		if(mAddWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mAddWordTaskId);
			mAddWordTaskId = null;
		}

		mIsDestroyed = true;
		super.onDestroy();
	}
	//==== events =============================================================
	public void onAddButton (View v)
	{
		boolean error = false;
		if(mAddWordTaskId != null)
			return;

		String dict = ((EditText)findViewById(R.id.first_addword_dictEdit)).getText().toString(); 
		String nativ = ((EditText)findViewById(R.id.first_addword_nativEdit)).getText().toString();
		String foreign = ((EditText)findViewById(R.id.first_addword_foreignEdit)).getText().toString();
		String toast = getResources().getString(R.string.addword_report_added);
		
		if(dict.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.first_addword_errorReport_dict);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.addword_report_empty);
			error = true;
		}
		
		if(nativ.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.first_addword_errorReport_nativ);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.addword_report_empty);
			error = true;
		}
		
		if(foreign.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.first_addword_errorReport_foreign);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.addword_report_empty);
			error = true;
		}	
		if(!error)
		{
			Intent intent = FirstAddWordTask.createIntent(nativ, foreign, dict, toast);
			mAddWordTaskId = getBcConnector().startTypicalTask(FirstAddWordTask.class, 
				intent, new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
	
						mAddWordTaskId = null;
						
						long dictId = bundle.getLong(BundleFields.DICT_ID);
	        			Editor ed = sp.edit();
	        			ed.putLong(PreferenceFields.DICT_ID, dictId);
	        			ed.commit();
	        			setResult(RESULT_OK);
						finish();
					}
				});
		}
	}
	public void onCancelButton (View v)
	{
		setResult(RESULT_CANCELED);
		finish();
	}
}