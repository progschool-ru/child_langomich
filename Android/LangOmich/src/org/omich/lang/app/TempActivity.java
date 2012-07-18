package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.app.words.AddWordActivity;
import org.omich.lang.app.words.GameActivity;
import org.omich.lang.app.words.WordsListActivity;
import org.omich.lang.apptool.activity.AppActivity;
import org.omich.tool.bcops.BcConnector;
import org.omich.tool.events.Listeners.IListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class TempActivity extends AppActivity implements OnSharedPreferenceChangeListener
{
	private SharedPreferences sp;	
	private BcConnector mBcConnector;
	private String mTheCorrectAccountTaskId;
	private boolean mIsDestroyed;	
	private TextView tvl;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_temp);
		mBcConnector = new BcConnector(this);
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sp.registerOnSharedPreferenceChangeListener(this);	
		tvl = (TextView)findViewById(R.id.item_temp_text_login);
		tvl.setText(sp.getString("login", ""));
		tvl.setTextColor(Color.WHITE);
		theCorrectAccount();		
	}
	@Override
	protected void onDestroy ()
	{
		if(mTheCorrectAccountTaskId != null)
		{
			mBcConnector.unsubscribeTask(mTheCorrectAccountTaskId);
			mTheCorrectAccountTaskId = null;
		}
		mIsDestroyed = true;		
		super.onDestroy();
	}	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		tvl.setText(sp.getString("login", ""));
		tvl.setTextColor(Color.WHITE);
		theCorrectAccount();
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
		mTheCorrectAccountTaskId = mBcConnector.startTypicalTask(TheCorrectAccountTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mTheCorrectAccountTaskId = null;
						
						if(bundle.getBoolean(BundleFields.CORRECT_ACCOUNT))	
							tvl.setTextColor(Color.GREEN);
						else
							tvl.setTextColor(Color.RED);
					}
				});
	}		
}
