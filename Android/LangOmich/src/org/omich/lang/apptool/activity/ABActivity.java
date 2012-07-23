package org.omich.lang.apptool.activity;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.IsLoggedInTask;
import org.omich.lang.app.TheCorrectAccountTask;
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
	private String mTheCorrectAccountTaskId;
	private String mIsLoggedInTaskId;	
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
		if(mTheCorrectAccountTaskId != null)
		{
			getBcConnector().unsubscribeTask(mTheCorrectAccountTaskId);
			mTheCorrectAccountTaskId = null;
		}
		if(mTheCorrectAccountTaskId != null)
		{
			getBcConnector().unsubscribeTask(mTheCorrectAccountTaskId);
			mTheCorrectAccountTaskId = null;
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

		Intent intent = TimingTask.createIntent(sp.getLong("mobileTime", 0), sp.getLong("serverTime", 0),sp.getString("cookie", ""));
		mTimingTaskId = getBcConnector().startTypicalTask(TimingTask.class, 
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
	protected void isLoggedIn()
	{
		if(mIsLoggedInTaskId != null)
			return;
		if(sp.getString("cookie", "").equals(""))
		{
			theCorrectAccount();
			return;
		}
		Intent intent = IsLoggedInTask.createIntent(sp.getString("cookie", ""));
		mIsLoggedInTaskId = getBcConnector().startTypicalTask(IsLoggedInTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mIsLoggedInTaskId = null;
						
						if(!bundle.getBoolean(BundleFields.IS_LOGGED_IN))					
							theCorrectAccount();
					}
				});
	}	
	protected void theCorrectAccount()
	{
		if(mTheCorrectAccountTaskId != null)
			return;

		Intent intent = TheCorrectAccountTask.createIntent(sp.getString("login", ""),sp.getString("password", ""));
		mTheCorrectAccountTaskId = getBcConnector().startTypicalTask(TheCorrectAccountTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mTheCorrectAccountTaskId = null;
						
						Editor ed = sp.edit();
						if(bundle.getBoolean(BundleFields.CORRECT_ACCOUNT))	
						    ed.putString("cookie", bundle.getString(BundleFields.COOKIE));														
						else
						    ed.putString("cookie", "");	
						ed.commit();
					}
				});
	}			
}