package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.app.words.AddWordActivity;
import org.omich.lang.app.words.GameActivity;
import org.omich.lang.app.words.WordsListActivity;
import org.omich.lang.apptool.activity.ABActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TempActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
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
		if(sp.getString("cookie", "").equals(""))
			tvl.setTextColor(Color.RED);
		else
			tvl.setTextColor(Color.GREEN);
		isLoggedIn();
	}		
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals("cookie") || key.equals("login"))
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
}
