package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.ABActivity;

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
	private DictsListAdapter mDictsAdapter;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);	
		
		mWordsAdapter = new WordsListAdapter(this, getBcConnector());
		mWordsAdapter.reloadItems();
		
		mDictsAdapter = new DictsListAdapter(this, getBcConnector());
		mDictsAdapter.reloadItems();
		
		Spinner sp = new Spinner(this);
		sp.setAdapter(mDictsAdapter);
		
		ListView lv = (ListView)findViewById(R.id.wordslist_list);
		lv.addHeaderView(sp);
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
		if(key.equals("isTiming"))
		{
			  if(sp.getBoolean("isTiming", false))
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
		mDictsAdapter.destroy();
		mDictsAdapter = null;

		super.onDestroy();
	}
	
	//=========================================================================
}
