package org.omich.lang.apptool.activity;

import org.omich.lang.R;

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
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) 
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
	        	if(!sp.getBoolean("isTiming", false))
	        	{
	        		Editor ed = sp.edit();
	    	    	ed.putBoolean("isTiming", true);
	    	    	ed.commit();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }		
}