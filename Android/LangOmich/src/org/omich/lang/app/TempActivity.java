package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.app.words.AddWordActivity;
import org.omich.lang.app.words.GameActivity;
import org.omich.lang.app.words.WordsListActivity;
import org.omich.lang.apptool.activity.ABActivity;
import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TempActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private String mTheCorrectAccountTaskId;
	private String mIsLoggedInTaskId;	
	private TextView tvl;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_temp);
		sp.registerOnSharedPreferenceChangeListener(this);	
		tvl = (TextView)findViewById(R.id.item_temp_text_login);
		tvl.setText(sp.getString("login", ""));
		tvl.setTextColor(Color.WHITE);		
		isLoggedIn();
	}	
	@Override
	protected void onDestroy ()
	{
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
		super.onDestroy();
	}	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals("cookie"))
		{		
			tvl.setText(sp.getString("login", ""));
			if(sp.getString("cookie", "").equals(""))
				tvl.setTextColor(Color.RED);
			else
				tvl.setTextColor(Color.GREEN);
		}
		else if(key.equals("isTiming"))
		{
			if(sp.getBoolean("isTiming", false))
				itemTiming.setIcon(R.drawable.ic_sunc_enable);
			else
				itemTiming.setIcon(R.drawable.ic_sunc_disable);			
		}
	}		
	//==== events =============================================================
	public void onAddWord (View v)
	{
		getForResultStarter().startForResult(new Intent(this, AddWordActivity.class), null);
	}	
	public void onShowWords (View v)
	{
		getForResultStarter().startForResult(new Intent(this, WordsListActivity.class), null);
	}
	public void onGame (View v)
	{
		getForResultStarter().startForResult(new Intent(this, GameActivity.class), null);
	}
	public void onSettings (View v)
	{
		getForResultStarter().startForResult(new Intent(this, SettingsActivity.class), null);
	}	
	//=========================================================================
	private void theCorrectAccount()
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
						{
							tvl.setTextColor(Color.GREEN);
						    ed.putString("cookie", bundle.getString(BundleFields.COOKIE));														
						}
						else
						{
							tvl.setTextColor(Color.RED);
						    ed.putString("cookie", "");	
						}
						ed.commit();
					}
				});
	}	
	private void isLoggedIn()
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
						
						if(bundle.getBoolean(BundleFields.CORRECT_ACCOUNT))	
							tvl.setTextColor(Color.GREEN);
						else
							theCorrectAccount();
					}
				});
	}		
}
