package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.ABActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private TextView tvl;
	private String mTheCorrectAccountTaskId;
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_settings);		
		sp.registerOnSharedPreferenceChangeListener(this);	
		tvl = (TextView)findViewById(R.id.item_settings_text_login);
		tvl.setText(sp.getString("login", ""));
		if(sp.getString("cookie", "").equals(""))
			tvl.setTextColor(Color.RED);
		else
			tvl.setTextColor(Color.GREEN);
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
		super.onDestroy();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (data == null) {return;}
	    Boolean isNewAccount = data.getBooleanExtra("isNewAccount", false);
	    if(isNewAccount)
		{
			tvl.setText(sp.getString("login", ""));	
			theCorrectAccount();	
    	}
	}		
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals("cookie"))
		{		
			tvl.setText(prefs.getString("login", ""));
			if(prefs.getString("cookie", "").equals(""))
				tvl.setTextColor(Color.RED);
			else
				tvl.setTextColor(Color.GREEN);
		}	
		else if(key.equals("isTiming"))
		{
			  if(prefs.getBoolean("isTiming", false))
				  itemTiming.setIcon(R.drawable.ic_sunc_enable);
			  else
				  itemTiming.setIcon(R.drawable.ic_sunc_disable);			
		}
	}	
	public void onNewAccount (View v)
	{
		getForResultStarter().startForResult(new Intent(this, NewAccountActivity.class), null);
	}
}