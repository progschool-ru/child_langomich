package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	private SharedPreferences sp;
	
	private boolean changeDictInPreferences;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_adddict);
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		changeDictInPreferences = getIntent().getBooleanExtra("changeDictInPreferences", true);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
		String taskAddText = getResources().getString(R.string.adddict_report_added);

		if(name.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.adddict_errorReport);
			errorView.setTextColor(getErrorColor());
			errorView.setText(R.string.adddict_report__empty);
		}
		else
		{
			Intent intent = AddDictTask.createIntent(name, taskAddText);
			mAddDictTaskId = getBcConnector().startTypicalTask(AddDictTask.class, intent, new IListener<Bundle>()
				{
					@Override
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
		
						mAddDictTaskId = null;
						
						long dictId = bundle.getLong(BundleFields.DICT_ID);
						if(dictId == -1)
						{
							TextView errorView = (TextView) findViewById(R.id.adddict_errorReport);
							errorView.setTextColor(getErrorColor());
							errorView.setText(R.string.adddict_report_in_base);														
						}
						else if(changeDictInPreferences)
						{
		        			Editor ed = sp.edit();
		        			ed.putLong(PreferenceFields.DICT_ID, dictId);
		        			ed.commit();
		        		    setResult(RESULT_OK);		        			
							finish();
						}
						else
						{
		        			Intent data = new Intent();
		        			data.putExtra("dictId", dictId);							
		        		    setResult(RESULT_OK, data);		        			
							finish();							
						}
					}
				});		
		}
	}
	public void onCancelButton (View v)
	{	
	    setResult(RESULT_CANCELED);			
		finish();
	}
	
	
	private int getErrorColor()
	{
		return getResources().getColor(R.color.lang_addDict_error);
	}
}
