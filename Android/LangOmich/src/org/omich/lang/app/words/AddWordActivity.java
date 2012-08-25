package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerInt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddWordActivity extends BcActivity
{
	private String mAddWordTaskId;
	
	private boolean mIsDestroyed;
	private DictSpinner dictSpinner;
	protected SharedPreferences sp;
	
	private final int REQUEST_CODE_ADD_DICT = 101;
	
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_add_word);
		dictSpinner = new DictSpinner((Spinner)findViewById(R.id.addword_dictSpinner), this, true, true, new IListenerInt()
		{
			public void handle (int key)
			{ 
				if(key == DictSpinner.ADD_DICT)
					startAddDictActivity();
			}			
		});
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	
	@Override
	protected void onDestroy ()
	{
		if(mAddWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mAddWordTaskId);
			mAddWordTaskId = null;
		}
		dictSpinner.destroy();
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//==== events =============================================================
	private void startAddDictActivity()
	{
	    Intent intent = new Intent(this, AddDictActivity.class);
	    startActivityForResult(intent, 1);		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == REQUEST_CODE_ADD_DICT)
		{
			if(resultCode == RESULT_OK)
			{
				if(data!= null)
					dictSpinner.reload(data.getLongExtra("dictId", DictSpinner.NULL_DICT));	
				else
					dictSpinner.reload(DictSpinner.NULL_DICT);
			}
			else if(resultCode == RESULT_CANCELED)
				dictSpinner.reload(DictSpinner.NULL_DICT);
		}
	}	

	public void onAddButton (View v)
	{
		boolean error = false;
		if(mAddWordTaskId != null)
			return;

		String nativ = ((EditText)findViewById(R.id.addword_nativEdit)).getText().toString();
		String foreign = ((EditText)findViewById(R.id.addword_foreignEdit)).getText().toString();
		String toast = getResources().getString(R.string.addword_report_added);

		if(nativ.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.addword_errorReport_nativ);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.addword_report_empty);
			error = true;
		}
		
		if(foreign.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.addword_errorReport_foreign);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.addword_report_empty);
			error = true;
		}	
		if(!error)
		{
			Intent intent = AddWordTask.createIntent(nativ, foreign, sp.getLong(PreferenceFields.DICT_ID, -1), toast);
			mAddWordTaskId = getBcConnector().startTypicalTask(AddWordTask.class, 
				intent, new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
	
						mAddWordTaskId = null;
						
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
