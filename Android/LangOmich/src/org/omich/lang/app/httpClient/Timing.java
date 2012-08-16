package org.omich.lang.app.httpClient;

import java.util.Date;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.IsLoggedInTask;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.TheCorrectAccountTask;
import org.omich.lang.apptool.activity.TimingTask;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.events.Listeners.IListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Timing 
{
	private String mTimingTaskId;
	private String mTheCorrectAccountTaskId;
	private String mIsLoggedInTaskId;
	
	private IBcConnector bcConnector;
	private Context context;
	private SharedPreferences sp;
	private boolean mIsDestroyed;
	
	public String successText;
	
	public Timing(Context context, IBcConnector bcConnector)
	{
		sp = PreferenceManager.getDefaultSharedPreferences(context);	
		this.context = context;
		this.bcConnector = bcConnector;
		successText = context.getResources().getString(R.string.timing_report_success);
	}
	public void timing()
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
				sp.getString(PreferenceFields.COOKIE, ""), successText);
		mTimingTaskId = bcConnector.startTypicalTask(TimingTask.class, 
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
	public void isLoggedIn()
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
		mIsLoggedInTaskId = bcConnector.startTypicalTask(IsLoggedInTask.class, 
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
	public void theCorrectAccount()
	{		
		
		if(mTheCorrectAccountTaskId != null)
			return;
		if(!hasInternetConnection())
			return;
		Intent intent = TheCorrectAccountTask.createIntent(sp.getString("login", ""),sp.getString("password", ""));
		mTheCorrectAccountTaskId = bcConnector.startTypicalTask(TheCorrectAccountTask.class, 
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
	private boolean hasInternetConnection()
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
	public void destroy()
	{
		if(mTimingTaskId != null)
		{
			bcConnector.unsubscribeTask(mTimingTaskId);
			mTimingTaskId = null;
		}	
		if(mTheCorrectAccountTaskId != null)
		{
			bcConnector.unsubscribeTask(mTheCorrectAccountTaskId);
			mTheCorrectAccountTaskId = null;
		}
		if(mTheCorrectAccountTaskId != null)
		{
			bcConnector.unsubscribeTask(mTheCorrectAccountTaskId);
			mTheCorrectAccountTaskId = null;
		}	
		mIsDestroyed = true;		
	}
}
