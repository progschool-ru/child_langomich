package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.ABActivity;
import org.omich.tool.events.Listeners.IListenerInt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;

public class WordsListActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private WordsListAdapter mWordsAdapter;
	private DictSpinner dictSpinner;
	
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);	
		
		sp.registerOnSharedPreferenceChangeListener(this);	
		
		mWordsAdapter = new WordsListAdapter(this, getBcConnector(), sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();
		
		dictSpinner = new DictSpinner((Spinner)findViewById(R.id.wordslist_spinner), this, new IListenerInt()
		{
			public void handle (int key)
			{ 
				if(key == dictSpinner.ADD_DICT)
					startAddDictActivity();
				else if(key == dictSpinner.SELECT_DICT)
					reload();
			}			
		});
		ListView lv = (ListView)findViewById(R.id.wordslist_list);
		lv.setAdapter(mWordsAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
//				Word word = (Word)mWordsAdapter.getItem(position-1);
			}
		});
	}
	private void startAddDictActivity()
	{
	    Intent intent = new Intent(this, AddDictActivity.class);
	    startActivityForResult(intent, 1);		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == 1 && resultCode == RESULT_OK && data != null)
		{
			if(data.getBooleanExtra("result", true))
			{
				reload();			
			}
			
		}
	}	
	private void reload()
	{
		mWordsAdapter.setNewDictId(sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();					
		dictSpinner.reload();			
	}
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals(PreferenceFields.IS_TIMING))
		{
			  if(prefs.getBoolean(PreferenceFields.IS_TIMING, false))
				  itemTiming.setIcon(R.drawable.ic_sunc_enable);
			  else
				  itemTiming.setIcon(R.drawable.ic_sunc_disable);			
		}	
	}		
	@Override
	protected void onDestroy ()
	{
		mWordsAdapter.destroy();
		mWordsAdapter = null;

		dictSpinner.destroy();
		
		super.onDestroy();
	}
	
	//=========================================================================
}
