package org.omich.lang.apptool.activity;

import java.util.Date;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.IsLoggedInTask;
import org.omich.lang.app.TheCorrectAccountTask;
import org.omich.tool.events.Listeners.IListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
		  if(sp.getBoolean(PreferenceFields.IS_TIMING, false))
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
	        	if(!sp.getBoolean(PreferenceFields.IS_TIMING, false))
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
		if(!hasInternetConnection())
			return;		
		Editor ed = sp.edit();
    	ed.putBoolean(PreferenceFields.IS_TIMING, true);
    	ed.commit();
		
		if(mTimingTaskId != null)
			return;

		Intent intent = TimingTask.createIntent(sp.getLong(PreferenceFields.MOBILE_TIME, 0), 
				sp.getLong(PreferenceFields.SERVER_TIME, 0), 
				sp.getString(PreferenceFields.COOKIE, ""));
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
						if(bundle.getLong(BundleFields.SERVER_TIME) != 0)
						{					
							Long mobileTime = new Date().getTime();
							ed.putLong(PreferenceFields.MOBILE_TIME, mobileTime);
							ed.putLong(PreferenceFields.SERVER_TIME, bundle.getLong(BundleFields.SERVER_TIME));
						}	        		
		    	    	ed.putBoolean(PreferenceFields.IS_TIMING, false);
		    	    	ed.commit();											
					}
				});
	}
	protected void isLoggedIn()
	{
		if(mIsLoggedInTaskId != null)
			return;
		if(!hasInternetConnection())
			return;		
		if(sp.getString(PreferenceFields.COOKIE, "").equals(""))
		{
			theCorrectAccount();
			return;
		}
		Intent intent = IsLoggedInTask.createIntent(sp.getString(PreferenceFields.COOKIE, ""));
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
		if(!hasInternetConnection())
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
						    ed.putString(PreferenceFields.COOKIE, bundle.getString(BundleFields.COOKIE));														
						else
						    ed.putString(PreferenceFields.COOKIE, "");	
						ed.commit();
					}
				});
	}	
	public boolean hasInternetConnection()
	{
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
}