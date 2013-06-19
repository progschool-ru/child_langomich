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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
		addOnEditListeners();
	}
	
	private void addOnEditListeners()
	{
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); 
		
		EditText dictName = ((EditText)findViewById(R.id.first_addword_dictEdit));
		EditText nativEdit = ((EditText)findViewById(R.id.first_addword_nativEdit));
		final EditText foreignEdit = ((EditText)findViewById(R.id.first_addword_foreignEdit));

		dictName.setText(getResources().getString(R.string.first_addword_text_exemple_dict));
		nativEdit.setText(getResources().getString(R.string.first_addword_text_exemple_nativ));
		nativEdit.requestFocus();
		nativEdit.setSelection(nativEdit.getText().length());
		foreignEdit.setText(getResources().getString(R.string.first_addword_text_exemple_foreign));
		
		OnEditorActionListener l1 = new OnEditorActionListener() 
		{			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if(actionId == EditorInfo.IME_ACTION_SEND)
				{
					foreignEdit.requestFocus();
					foreignEdit.setSelection(foreignEdit.getText().length());
					handled = true;
				}
				return handled;
			}
		};
		
		OnEditorActionListener l2 = new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				boolean  handled = false;
				if(actionId == EditorInfo.IME_ACTION_SEND)
				{
					onAddButton(v);
					handled = true;
				}
				return handled;
			}
		};
		
		nativEdit.setOnEditorActionListener(l1);
		foreignEdit.setOnEditorActionListener(l2);
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

		String dict = ((EditText)findViewById(R.id.first_addword_dictEdit)).getText().toString().trim(); 
		String nativ = ((EditText)findViewById(R.id.first_addword_nativEdit)).getText().toString().trim();
		String foreign = ((EditText)findViewById(R.id.first_addword_foreignEdit)).getText().toString().trim();
		String toast = getResources().getString(R.string.addword_report_added);
		
		TextView errorViewDict = (TextView) findViewById(R.id.first_addword_errorReport_dict);
		TextView errorViewNativ = (TextView) findViewById(R.id.first_addword_errorReport_nativ);
		TextView errorViewForeign = (TextView) findViewById(R.id.first_addword_errorReport_foreign);
		
		errorViewDict.setText("");
		errorViewNativ.setText("");
		errorViewForeign.setText("");
		
		if(dict.equals(""))
		{
			errorViewDict.setTextColor(getErrorColor());
			errorViewDict.setText(R.string.addword_report_empty);
			error = true;
		}
		
		if(nativ.equals(""))
		{
			errorViewNativ.setTextColor(getErrorColor());
			errorViewNativ.setText(R.string.addword_report_empty);
			error = true;
		}
		
		if(foreign.equals(""))
		{
			errorViewForeign.setTextColor(getErrorColor());
			errorViewForeign.setText(R.string.addword_report_empty);
			error = true;
		}	
		if(!error)
		{
			Intent intent = FirstAddWordTask.createIntent(nativ, foreign, dict, toast);
			mAddWordTaskId = getBcConnector().startTypicalTask(FirstAddWordTask.class, 
				intent, new IListener<Bundle>()
				{
					@Override
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
	
	private int getErrorColor()
	{
		return getResources().getColor(R.color.lang_addWord_error);
	}

}