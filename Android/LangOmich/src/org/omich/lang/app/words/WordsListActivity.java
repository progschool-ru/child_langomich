package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.ABActivity;
import org.omich.tool.events.Listeners.IListenerBoolean;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

public class WordsListActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private WordsListAdapter mWordsAdapter;
	private DictsListAdapter mDictsAdapter;
	private Spinner spinner;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);	
		
		sp.registerOnSharedPreferenceChangeListener(this);	
		
		mWordsAdapter = new WordsListAdapter(this, getBcConnector(), sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();
		
		mDictsAdapter = new DictsListAdapter(this, getBcConnector(), new IListenerBoolean()
		{
			public void handle (boolean b) { if(b) spinner.setSelection(sp.getInt(PreferenceFields.DICT_POSITION, 0)); }			
		});
		mDictsAdapter.reloadItems();
		
		spinner = (Spinner)findViewById(R.id.wordslist_spinner);		
		spinner.setAdapter(mDictsAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{
        		long dictId = ((Dict)spinner.getSelectedItem()).dictId;
        		Editor ed = sp.edit();
            	ed.putInt(PreferenceFields.DICT_POSITION, position);
            	ed.putLong(PreferenceFields.DICT_ID, dictId);
            	ed.commit();
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
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
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals(PreferenceFields.IS_TIMING))
		{
			  if(sp.getBoolean(PreferenceFields.IS_TIMING, false))
				  itemTiming.setIcon(R.drawable.ic_sunc_enable);
			  else
				  itemTiming.setIcon(R.drawable.ic_sunc_disable);			
		}
		else if(key.equals(PreferenceFields.DICT_ID))
		{
			mWordsAdapter.setNewDictId(sp.getLong(PreferenceFields.DICT_ID, -1));
			mWordsAdapter.reloadItems();
		}		
	}		
	@Override
	protected void onDestroy ()
	{
		mWordsAdapter.destroy();
		mWordsAdapter = null;
		mDictsAdapter.destroy();
		mDictsAdapter = null;

		super.onDestroy();
	}
	
	//=========================================================================
}
