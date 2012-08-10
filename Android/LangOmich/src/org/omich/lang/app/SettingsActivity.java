package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.app.httpClient.Timing;
import org.omich.lang.apptool.activity.ABActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private TextView tvl;
	private SharedPreferences sp;
	Timing timing;
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_settings);	
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		sp.registerOnSharedPreferenceChangeListener(this);
		timing = new Timing(this, getBcConnector());
		tvl = (TextView)findViewById(R.id.item_settings_text_login);
		tvl.setText(sp.getString("login", ""));	
		if(sp.getString(PreferenceFields.COOKIE, "").equals(""))
			tvl.setTextColor(Color.RED);
		else
			tvl.setTextColor(Color.GREEN);
		timing.isLoggedIn();
	}
	@Override
	protected void onDestroy ()
	{
		timing.destroy();
		super.onDestroy();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (data == null) {return;}
	    Boolean isNewAccount = data.getBooleanExtra("isNewAccount", true);
	    if(isNewAccount)
		{
			tvl.setText(sp.getString("login", ""));	
			timing.theCorrectAccount();	
    	}
	}		
	public void onNewAccount (View v)
	{
		getForResultStarter().startForResult(new Intent(this, NewAccountActivity.class), null);
	}
	public void onTiming (View v)
	{
		timing.timing();
	}
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		if(key.equals(PreferenceFields.COOKIE))
		{
			if(sp.getString(PreferenceFields.COOKIE, "").equals(""))
				tvl.setTextColor(Color.RED);
			else
				tvl.setTextColor(Color.GREEN);
		}
	}	
}