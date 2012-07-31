package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.ABActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private TextView tvl;
	private EditText etin;
	private String mTheCorrectAccountTaskId;
	
	private SeekBar sb0;
	private SeekBar sb1;
	private SeekBar sb2;
	private SeekBar sb3;
	private SeekBar sb4;
	private SeekBar sb5;
	private SeekBar sb6;
	private SeekBar sb7;
	private SeekBar sb8;
	private SeekBar sb9;
	
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_settings);		
		sp.registerOnSharedPreferenceChangeListener(this);	
		tvl = (TextView)findViewById(R.id.item_settings_text_login);
		etin = (EditText)findViewById(R.id.item_settings_edit_idealNumber);
		
		sb0 = (SeekBar)findViewById(R.id.seekBar0);
		sb1 = (SeekBar)findViewById(R.id.seekBar1);
		sb2 = (SeekBar)findViewById(R.id.seekBar2);
		sb3 = (SeekBar)findViewById(R.id.seekBar3);
		sb4 = (SeekBar)findViewById(R.id.seekBar4);
		sb5 = (SeekBar)findViewById(R.id.seekBar5);
		sb6 = (SeekBar)findViewById(R.id.seekBar6);
		sb7 = (SeekBar)findViewById(R.id.seekBar7);
		sb8 = (SeekBar)findViewById(R.id.seekBar8);
		sb9 = (SeekBar)findViewById(R.id.seekBar9);
		
		tvl.setText(sp.getString("login", ""));
		if(sp.getString("cookie", "").equals(""))
			tvl.setTextColor(Color.RED);
		else
			tvl.setTextColor(Color.GREEN);
		etin.setText(Integer.toString(sp.getInt(PreferenceFields.IDEAL_NUMBER, 5)));
		
		sb0.setProgress(sp.getInt(PreferenceFields.WEIGHT_ZERO, 100) - 1);
		sb1.setProgress(sp.getInt(PreferenceFields.WEIGHT_ONE, 80) - 1);
		sb2.setProgress(sp.getInt(PreferenceFields.WEIGHT_TWO, 60) - 1);
		sb3.setProgress(sp.getInt(PreferenceFields.WEIGHT_THREE, 40) - 1);
		sb4.setProgress(sp.getInt(PreferenceFields.WEIGHT_FOUR, 30) - 1);
		sb5.setProgress(sp.getInt(PreferenceFields.WEIGHT_FIVE, 20) - 1);
		sb6.setProgress(sp.getInt(PreferenceFields.WEIGHT_SIX, 10) - 1);
		sb7.setProgress(sp.getInt(PreferenceFields.WEIGHT_SEVEN, 5) - 1);
		sb8.setProgress(sp.getInt(PreferenceFields.WEIGHT_EIGHT, 3) - 1);
		sb9.setProgress(sp.getInt(PreferenceFields.WEIGHT_NINE, 1) - 1);
		
		isLoggedIn();
	}
	@Override
	protected void onPause()
	{
		Editor ed = sp.edit();
		ed.putInt(PreferenceFields.IDEAL_NUMBER, Integer.valueOf(etin.getText().toString()));
		
		ed.putInt(PreferenceFields.WEIGHT_ZERO, sb0.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_ONE, sb1.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_TWO, sb2.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_THREE, sb3.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_FOUR, sb4.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_FIVE, sb5.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_SIX, sb6.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_SEVEN, sb7.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_EIGHT, sb8.getProgress()+1);
		ed.putInt(PreferenceFields.WEIGHT_NINE, sb9.getProgress()+1);
		
		ed.commit();
		super.onPause();
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
	    Boolean isNewAccount = data.getBooleanExtra("isNewAccount", true);
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