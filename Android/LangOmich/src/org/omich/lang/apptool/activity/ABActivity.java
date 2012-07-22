package org.omich.lang.apptool.activity;

import java.util.ArrayList;
import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.Word;
import org.omich.tool.events.Listeners.IListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ABActivity extends BcActivity
{
	protected MenuItem itemTiming;
	protected SharedPreferences sp;
	protected boolean mIsDestroyed;	
	private String mTimingTaskId;
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	@Override
	protected void onDestroy ()
	{
		if(mTimingTaskId != null)
		{
			getBcConnector().unsubscribeTask(mTimingTaskId);
			mTimingTaskId = null;
		}		
		mIsDestroyed = true;
		super.onDestroy();
	}	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.menu, menu);		  
		  itemTiming = menu.findItem(R.id.app_menu_item_timing);
		  if(sp.getBoolean("isTiming", false))
			  itemTiming.setIcon(R.drawable.ic_sunc_enable);
		  else
			  itemTiming.setIcon(R.drawable.ic_sunc_disable);
		  return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) 
        {
	        case R.id.app_menu_item_timing:
	        	timing();
	        	if(!sp.getBoolean("isTiming", false))
	        	{
	        		timing();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }	
	private void timing()
	{
		Editor ed = sp.edit();
    	ed.putBoolean("isTiming", true);
    	ed.commit();
		
		if(mTimingTaskId != null)
			return;

		Intent intent = TimingGetWordsTask.createIntent(sp.getLong("mobileTime", 0));
		mTimingTaskId = getBcConnector().startTypicalTask(TimingGetWordsTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mTimingTaskId = null;
						
						ArrayList<Word> words = bundle.getParcelableArrayList(BundleFields.WORDS_LIST);	
//						ArrayList<Dict> dicts = bundle.getParcelableArrayList(BundleFields.DICTS_LIST);
		        		Editor ed = sp.edit();
		    	    	ed.putBoolean("isTiming", false);
		    	    	ed.commit();						
						//send(words,dicts);						
					}
				});
	}
	private void send(ArrayList<Word> words,ArrayList<Dict> dicts)
	{
		if(mTimingTaskId != null)
			return;

		Intent intent = TimingSendWordsTask.createIntent(words, dicts, sp.getLong("serverTime", 0),sp.getString("cookie", ""));
		mTimingTaskId = getBcConnector().startTypicalTask(TimingSendWordsTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mTimingTaskId = null;
						
						ArrayList<Word> words = bundle.getParcelableArrayList(BundleFields.WORDS_LIST);	
						ArrayList<Dict> dicts = bundle.getParcelableArrayList(BundleFields.DICTS_LIST);						
						
						Editor ed = sp.edit();
						ed.putLong("serverTime", bundle.getLong(BundleFields.SERVER_TIME));	
						ed.commit();
						
						addWords(words, dicts);
					}
				});		
	}
	private void addWords(ArrayList<Word> words,ArrayList<Dict> dicts)
	{
		if(mTimingTaskId != null)
			return;

		Intent intent = TimingAddWordsTask.createIntent(words, dicts);
		mTimingTaskId = getBcConnector().startTypicalTask(TimingAddWordsTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mTimingTaskId = null;	
						
		        		Editor ed = sp.edit();
		    	    	ed.putBoolean("isTiming", false);
		    	    	ed.commit();						
					}
				});			
	}
}