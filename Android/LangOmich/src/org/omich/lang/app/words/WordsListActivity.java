package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.BcActivity;

import android.os.Bundle;
import android.widget.ListView;

public class WordsListActivity extends BcActivity
{
	private boolean mIsDestroyed;
	
	private WordsListAdapter mAdapter;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);
		
		mAdapter = new WordsListAdapter(this);
		mAdapter.reloadWords(getBcConnector());
		
		ListView lv = (ListView)findViewById(R.id.wordslist_list);
		lv.setAdapter(mAdapter);
	}
	
	@Override
	protected void onDestroy ()
	{
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//=========================================================================
}
