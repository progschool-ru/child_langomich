package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.Spinner;

public class AddWordActivity extends BcActivity implements OnSharedPreferenceChangeListener
{
	private String mAddWordTaskId;
	
	private boolean mIsDestroyed;
	private DictSpinner dictSpinner;
	protected SharedPreferences sp;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_addword);
		dictSpinner = new DictSpinner((Spinner)findViewById(R.id.addword_dictSpinner), this);
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sp.registerOnSharedPreferenceChangeListener(this);	
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals(PreferenceFields.DICT_ID))
		{
			dictSpinner.reload();
		}		
	}		
	public void onAddButton (View v)
	{
		if(mAddWordTaskId != null)
			return;

		String nativ = ((EditText)findViewById(R.id.addword_nativEdit)).getText().toString();
		String foreign = ((EditText)findViewById(R.id.addword_foreignEdit)).getText().toString();
		String toast = getResources().getString(R.string.dialogAddWordMain_Added);

		Intent intent = AddWordTask.createIntent(nativ, foreign, sp.getLong(PreferenceFields.DICT_ID, -1), toast);
		mAddWordTaskId = getBcConnector().startTypicalTask(AddWordTask.class, intent, new IListener<Bundle>()
			{
				public void handle (Bundle bundle)
				{
					if(mIsDestroyed)
						return;
	
					mAddWordTaskId = null;
				}
			});
		finish();
	}
}
