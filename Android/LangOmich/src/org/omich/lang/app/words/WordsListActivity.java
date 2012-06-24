package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.BcActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

public class WordsListActivity extends BcActivity
{
	private boolean mIsDestroyed;
	
	private WordsListAdapter mWordsAdapter;
	private DictsListAdapter mDictsAdapter;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);
		
		mWordsAdapter = new WordsListAdapter(this);
		mWordsAdapter.reloadWords(getBcConnector());
		
		mDictsAdapter = new DictsListAdapter(this);
		mDictsAdapter.reloadDicts(getBcConnector());
		
		Spinner sp = new Spinner(this);
		sp.setAdapter(mDictsAdapter);
		
		ListView lv = (ListView)findViewById(R.id.wordslist_list);
		lv.addHeaderView(sp);
		lv.setAdapter(mWordsAdapter);
	}
	
	@Override
	protected void onDestroy ()
	{
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//=========================================================================
}
