package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends BcActivity implements OnSharedPreferenceChangeListener
{
	private SharedPreferences sp;
	private TextView tvl;
	private String mTheCorrectAccountTaskId;
	private boolean mIsDestroyed;
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_settings);
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sp.registerOnSharedPreferenceChangeListener(this);	
		tvl = (TextView)findViewById(R.id.item_settings_text_login);
		tvl.setText(sp.getString("login", ""));
		tvl.setTextColor(Color.WHITE);
		theCorrectAccount();
	}
	@Override
	protected void onDestroy ()
	{
		if(mTheCorrectAccountTaskId != null)
		{
			getBcConnector().unsubscribeTask(mTheCorrectAccountTaskId);
			mTheCorrectAccountTaskId = null;
		}
		
		mIsDestroyed = true;
		super.onDestroy();
	}	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals("login"))
		{
			tvl.setText(sp.getString("login", ""));
			tvl.setTextColor(Color.WHITE);
			theCorrectAccount();
		}
	}	
	public void onNewAccount (View v)
	{
		getForResultStarter().startForResult(new Intent(this, NewAccountActivity.class), null);
	}	
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
						
						if(bundle.getBoolean(BundleFields.CORRECT_ACCOUNT))	
						{
							tvl.setTextColor(Color.GREEN);
						    Editor ed = sp.edit();
						    ed.putString("cookie", bundle.getString(BundleFields.COOKIE));
						    ed.commit();		
						}
						else
							tvl.setTextColor(Color.RED);
						

					}
				});
	}	
}